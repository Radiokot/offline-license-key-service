package ua.com.radiokot.license.service.btcpay.greenfield.invocies.model

import com.fasterxml.jackson.annotation.JsonProperty

class GreenfieldInvoiceCreationData(
    /**
     * Decimal amount with "." separator.
     * If null or unspecified, the invoice will be a top-up invoice.
     */
    @JsonProperty("amount")
    val amount: String?,

    /**
     * BTC, USD, etc.
     * If null, empty or unspecified, the currency will be the store's settings default.
     */
    @JsonProperty("currency")
    val currency: String,

    /**
     * Additional settings to customize the checkout flow.
     */
    @JsonProperty("checkout")
    val checkout: GreenfieldInvoice.CheckoutData? = null,

    /**
     * Additional information around the invoice that can be supplied.
     */
    @JsonProperty("metadata")
    val metadata: GreenfieldInvoice.Metadata? = null,
)
