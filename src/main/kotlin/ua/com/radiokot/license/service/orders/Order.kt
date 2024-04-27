package ua.com.radiokot.license.service.orders

import java.math.BigDecimal

data class Order(
    val id: String,
    val paymentMethodId: String,
    val amount: BigDecimal,
    val currency: String,
    val buyerEmail: String,
    val encodedKey: String,
    val status: Status,
    val absoluteUrl: String,
) {
    override fun equals(other: Any?): Boolean =
        id == other

    override fun hashCode(): Int =
        id.hashCode()

    enum class Status {
        PENDING,
        PAID,
        CLOSED,
        ;
    }
}
