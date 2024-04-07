package ua.com.radiokot.license.service.btcpay.greenfield.invocies

import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoiceCreationData

class RealGreenfieldStoreInvoicesService(
    private val storeId: String,
    private val authorization: String,
    private val greenfieldInvoicesApi: GreenfieldInvoicesApi,
) : GreenfieldStoreInvoicesService {
    override fun getInvoices(orderId: Iterable<String>): List<GreenfieldInvoice> =
        greenfieldInvoicesApi.getInvoices(
            authorization = authorization,
            storeId = storeId,
            orderId = orderId,
        )
            .execute()
            .body()
            ?: error("The response must have a body")

    override fun createInvoice(creationData: GreenfieldInvoiceCreationData): GreenfieldInvoice =
        greenfieldInvoicesApi.createInvoice(
            authorization = authorization,
            storeId = storeId,
            creationData = creationData,
        )
            .execute()
            .body()
            ?: error("The response must have a body")
}
