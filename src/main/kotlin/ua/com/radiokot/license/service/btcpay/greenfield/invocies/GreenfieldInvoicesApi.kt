package ua.com.radiokot.license.service.btcpay.greenfield.invocies

import retrofit2.http.*
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoiceCreationData

interface GreenfieldInvoicesApi {
    @Headers(
        "Accept: application/json",
    )
    @GET("api/v1/stores/{storeId}/invoices")
    fun getInvoices(
        @Path("storeId")
        storeId: String,
        @Query("orderId")
        orderId: Iterable<String>,
    ): List<GreenfieldInvoice>

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
    )
    @POST("api/v1/stores/{storeId}/invoices")
    fun createInvoice(
        @Path("storeId")
        storeId: String,
        @Body
        creationData: GreenfieldInvoiceCreationData,
    ): GreenfieldInvoice

    @Headers(
        "Accept: application/json",
    )
    @GET("api/v1/stores/{storeId}/invoices/{invoiceId}")
    fun getInvoice(
        @Path("storeId")
        storeId: String,
        @Path("invoiceId")
        invoiceId: String,
    ): GreenfieldInvoice
}
