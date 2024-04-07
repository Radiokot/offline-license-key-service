package ua.com.radiokot.license.service.btcpay.greenfield.invocies

import retrofit2.Call
import retrofit2.http.*
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoiceCreationData

interface GreenfieldInvoicesApi {
    @Headers(
        "Accept: application/json",
    )
    @GET("api/v1/stores/{storeId}/invoices")
    fun getInvoices(
        @Header("Authorization")
        authorization: String,
        @Path("storeId")
        storeId: String,
        @Query("orderId")
        orderId: Iterable<String>,
    ): Call<List<GreenfieldInvoice>>

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
    )
    @POST("api/v1/stores/{storeId}/invoices")
    fun createInvoice(
        @Header("Authorization")
        authorization: String,
        @Path("storeId")
        storeId: String,
        @Body
        creationData: GreenfieldInvoiceCreationData,
    ): Call<GreenfieldInvoice>
}
