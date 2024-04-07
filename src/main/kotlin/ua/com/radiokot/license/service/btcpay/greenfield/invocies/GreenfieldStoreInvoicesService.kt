package ua.com.radiokot.license.service.btcpay.greenfield.invocies

import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoiceCreationData

interface GreenfieldStoreInvoicesService {
    /**
     * View information about the existing invoices in the store.
     *
     * @param orderId Array of OrderIds to fetch the invoices for ([GreenfieldInvoice.Metadata.orderId])
     * @see <a href="https://docs.btcpayserver.org/API/Greenfield/v1/#operation/Invoices_GetInvoices">API reference</a>
     */
    fun getInvoices(orderId: Iterable<String>): List<GreenfieldInvoice>

    /**
     * Create a new invoice in the store.
     *
     * @see <a href="https://docs.btcpayserver.org/API/Greenfield/v1/#operation/Invoices_CreateInvoice">API reference</a>
     */
    fun createInvoice(creationData: GreenfieldInvoiceCreationData): GreenfieldInvoice
}
