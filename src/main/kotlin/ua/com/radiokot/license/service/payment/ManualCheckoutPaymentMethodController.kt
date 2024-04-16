package ua.com.radiokot.license.service.payment

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import ua.com.radiokot.license.service.orders.OrdersRepository

class ManualCheckoutPaymentMethodController(
    private val ordersRepository: OrdersRepository,
) {
    fun checkout(ctx: Context) = with(ctx) {
        val orderId = pathParam("orderId")
        val order = ordersRepository.getOrderById(orderId)
            ?: throw NotFoundResponse("Order '$orderId' not found")

        if (order.paymentMethodId != PAYMENT_METHOD_ID) {
            throw BadRequestResponse("Order '$orderId' uses different payment method")
        }

        result("""
            Please, contact me by email or via Telegram providing your order ID: $orderId
        """.trimIndent())
    }

    companion object {
        const val PAYMENT_METHOD_ID = "manual"
    }
}
