package ua.com.radiokot.license.service.payment

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.GreenfieldInvoicesApi
import ua.com.radiokot.license.service.orders.OrdersRepository

typealias BtcPayInvoiceUrlFactory = (invoiceId: String) -> String

class BtcPayPaymentMethodController(
    private val storeId: String,
    private val invoiceUrlFactory: BtcPayInvoiceUrlFactory,
    private val ordersRepository: OrdersRepository,
    private val greenfieldInvoicesApi: GreenfieldInvoicesApi,
) {
    fun checkout(ctx: Context) = with(ctx) {
        val orderId = pathParam("orderId")
        val order = ordersRepository.getOrderById(orderId)
            ?: throw NotFoundResponse("Order '$orderId' not found")

        if (order.paymentMethodId != PAYMENT_METHOD_ID) {
            throw BadRequestResponse("Order '$orderId' uses different payment method")
        }

        val invoice = greenfieldInvoicesApi
            .getInvoices(
                storeId = storeId,
                orderId = setOf(orderId),
            )
            .firstOrNull()
            ?: error("Can't find invoice for order '$orderId'")

        redirect(invoiceUrlFactory(invoice.id))
    }

    companion object {
        const val PAYMENT_METHOD_ID = "btcpay"
    }
}
