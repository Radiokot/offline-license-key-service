package ua.com.radiokot.license.service.orders

import java.math.BigDecimal

interface OrderRepository {
    fun createOrder(
        id: String,
        paymentMethodId: String,
        amount: BigDecimal,
        currency: String,
        buyerEmail: String,
        encodedKey: String,
    ): Order

    fun getOrderById(orderId: String): Order?
}
