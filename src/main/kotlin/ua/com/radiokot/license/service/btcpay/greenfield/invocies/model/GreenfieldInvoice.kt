package ua.com.radiokot.license.service.btcpay.greenfield.invocies.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

class GreenfieldInvoice(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("amount")
    val amount: String,
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("status")
    val status: Status,
    @JsonProperty("metadata")
    val metadata: Metadata?,
    @JsonProperty("checkout")
    val checkoutData: CheckoutData?,
) {

    class CheckoutData(
        @JsonProperty("speedPolicy")
        val speedPolicy: SpeedPolicy? = null,
        @JsonProperty("redirectURL")
        val redirectUrl: String? = null,
        @JsonProperty("redirectAutomatically")
        val redirectAutomatically: Boolean? = null,
        @JsonProperty("lazyPaymentMethods")
        val lazyPaymentMethods: Boolean? = null,
    )

    class Metadata(
        /**
         * Visible in the invoice details view.
         */
        @JsonProperty("buyerEmail")
        val buyerEmail: String? = null,

        /**
         * Refers to the order ID from an external system.
         * Visible in the invoice details view.
         * This property is indexed, allowing for efficient invoice searches.
         */
        @JsonProperty("orderId")
        val orderId: String? = null,

        /**
         * Private extra data to include.
         */
        @JsonProperty("_extra")
        val extra: JsonNode? = null,
    )

    enum class SpeedPolicy {
        @JsonProperty("HighSpeed")
        HIGH,

        @JsonProperty("MediumSpeed")
        MEDIUM,

        @JsonProperty("LowMediumSpeed")
        LOW_MEDIUM,

        @JsonProperty("LowSpeed")
        LOW,
        ;
    }

    enum class Status {
        @JsonProperty("Expired")
        EXPIRED,

        @JsonProperty("Invalid")
        INVALID,

        @JsonProperty("New")
        NEW,

        @JsonProperty("Processing")
        PROCESSING,

        @JsonProperty("Settled")
        SETTLED,
        ;
    }
}
