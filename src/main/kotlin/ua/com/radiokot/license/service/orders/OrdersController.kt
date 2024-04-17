package ua.com.radiokot.license.service.orders

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import java.math.BigDecimal

typealias OrderCheckoutUrlFactory = (
    orderId: String,
    paymentMethodId: String,
) -> String

class OrdersController(
    private val orderCheckoutUrlFactory: OrderCheckoutUrlFactory,
    private val ordersRepository: OrdersRepository,
) {
    fun getOrderById(ctx: Context) = with(ctx) {
        val orderId = pathParam("orderId")
        val order = ordersRepository.getOrderById(orderId)
            ?: throw NotFoundResponse("Order '$orderId' not found")

        when (order.status) {
            Order.Status.PENDING ->
                redirect(orderCheckoutUrlFactory(orderId, order.paymentMethodId))

            Order.Status.PAID ->
                result("Thank you! Your key is: ${order.encodedKey}")

            Order.Status.CLOSED ->
                result("This order is closed. Try ordering once again")
        }
    }

    fun createOrder(ctx: Context) = with(ctx) {
        val email = formParam("email")?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing email")
        val hardware = formParam("hardware")?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing hardware")
        val features = formParam("features")?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing features")
        val paymentMethod = queryParam("method")?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing payment method")

        val createdOrder = ordersRepository.createOrder(
            id = System.currentTimeMillis().toString(),
            paymentMethodId = paymentMethod,
            amount = BigDecimal.TEN,
            currency = "USD",
            buyerEmail = email,
            encodedKey = features,
        )

        redirect(orderCheckoutUrlFactory(createdOrder.id, createdOrder.paymentMethodId))
    }
}
