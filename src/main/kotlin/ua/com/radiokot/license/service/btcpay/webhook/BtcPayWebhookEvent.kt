package ua.com.radiokot.license.service.btcpay.webhook

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class BtcPayWebhookEvent
@JsonCreator
constructor(
    @JsonProperty("type")
    val type: Type,
    @JsonProperty("storeId")
    val storeId: String,
    @JsonProperty("invoiceId")
    val invoiceId: String,
) {
    enum class Type {
        @JsonProperty("InvoiceCreated")
        INVOICE_CREATED,

        @JsonProperty("InvoiceReceivedPayment")
        INVOICE_RECEIVED_PAYMENT,

        @JsonProperty("InvoiceProcessing")
        INVOICE_PROCESSING,

        @JsonProperty("InvoiceExpired")
        INVOICE_EXPIRED,

        @JsonProperty("InvoiceSettled")
        INVOICE_SETTLED,

        @JsonProperty("InvoiceInvalid")
        INVOICE_INVALID,

        @JsonProperty("InvoicePaymentSettled")
        INVOICE_PAYMENT_SETTLED,
        ;
    }
}
