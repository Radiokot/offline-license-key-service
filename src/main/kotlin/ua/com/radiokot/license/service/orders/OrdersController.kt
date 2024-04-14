package ua.com.radiokot.license.service.orders

import io.javalin.http.Context
import io.javalin.http.NotFoundResponse

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
}
