package ua.com.radiokot.license.service.orders

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.io.BigDecimalParser
import com.fasterxml.jackson.databind.ObjectMapper
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.GreenfieldInvoicesApi
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoiceCreationData
import java.math.BigDecimal

typealias OrderUrlFactory = (orderId: String) -> String

class BtcPayOrdersRepository(
    private val storeId: String,
    private val orderUrlFactory: OrderUrlFactory,
    private val speedPolicy: GreenfieldInvoice.SpeedPolicy,
    private val greenfieldInvoicesApi: GreenfieldInvoicesApi,
    private val jsonObjectMapper: ObjectMapper,
) : OrdersRepository {
    override fun createOrder(
        id: String,
        paymentMethodId: String,
        amount: BigDecimal,
        currency: String,
        buyerEmail: String,
        encodedKey: String
    ): Order {
        check(getOrderById(id) == null) {
            "Order with id '$id' already exists"
        }

        return greenfieldInvoicesApi.createInvoice(
            storeId = storeId,
            creationData = GreenfieldInvoiceCreationData(
                amount = amount.toPlainString(),
                currency = currency,
                checkout = GreenfieldInvoice.CheckoutData(
                    speedPolicy = speedPolicy,
                    redirectUrl = orderUrlFactory(id),
                    redirectAutomatically = true,
                ),
                metadata = GreenfieldInvoice.Metadata(
                    orderId = id,
                    buyerEmail = buyerEmail,
                    extra = jsonObjectMapper.valueToTree(
                        MetadataExtra(
                            paymentMethodId = paymentMethodId,
                            encodedKey = encodedKey,
                        )
                    )
                )
            )
        ).toOrder()
    }

    override fun getOrderById(orderId: String): Order? =
        greenfieldInvoicesApi.getInvoices(
            storeId = storeId,
            orderId = setOf(orderId),
        ).firstOrNull()?.toOrder()

    private fun GreenfieldInvoice.toOrder(): Order {
        val metadata = checkNotNull(this.metadata) {
            "The invoice must have the metadata"
        }
        val metadataExtra: MetadataExtra =
            jsonObjectMapper.treeToValue(
                checkNotNull(metadata.extra) {
                    "The invoice must have the metadata extra"
                },
                MetadataExtra::class.java,
            )

        return Order(
            id = checkNotNull(metadata.orderId) {
                "The invoice must have the order ID"
            },
            paymentMethodId = metadataExtra.paymentMethodId,
            amount = BigDecimalParser.parse(this.amount),
            currency = this.currency,
            buyerEmail = checkNotNull(metadata.buyerEmail) {
                "The invoice must have the buyer email"
            },
            encodedKey = metadataExtra.encodedKey,
            status = when (this.status) {
                GreenfieldInvoice.Status.EXPIRED,
                GreenfieldInvoice.Status.INVALID ->
                    Order.Status.CLOSED

                GreenfieldInvoice.Status.NEW,
                GreenfieldInvoice.Status.PROCESSING ->
                    Order.Status.PENDING

                GreenfieldInvoice.Status.SETTLED ->
                    Order.Status.PAID
            }
        )
    }

    private class MetadataExtra(
        @JsonProperty("paymentMethodId")
        val paymentMethodId: String,
        @JsonProperty("encodedKey")
        val encodedKey: String,
    )
}
