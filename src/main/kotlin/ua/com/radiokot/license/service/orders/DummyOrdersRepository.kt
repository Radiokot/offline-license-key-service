package ua.com.radiokot.license.service.orders

import java.math.BigDecimal
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DummyOrdersRepository(
    private val orderAbsoluteUrlFactory: OrderAbsoluteUrlFactory,
) : OrdersRepository {
    private val orders: MutableMap<String, Order> = mutableMapOf()
    private val closingExecutor = Executors.newSingleThreadScheduledExecutor()

    override fun createOrder(
        id: String,
        paymentMethodId: String,
        amount: BigDecimal,
        currency: String,
        buyerEmail: String,
        encodedKey: String
    ): Order {
        val order = Order(
            id = id,
            paymentMethodId = paymentMethodId,
            amount = amount,
            currency = currency,
            buyerEmail = buyerEmail,
            encodedKey = encodedKey,
            status = Order.Status.PENDING,
            absoluteUrl = orderAbsoluteUrlFactory.getOrderAbsoluteUrl(id),
        )
        orders[order.id] = order

        closingExecutor.schedule({
            orders[order.id] = order.copy(
                status = Order.Status.PAID,
            )
        }, 3, TimeUnit.SECONDS)

        return order
    }

    override fun getOrderById(orderId: String): Order? =
        orders[orderId]
}
