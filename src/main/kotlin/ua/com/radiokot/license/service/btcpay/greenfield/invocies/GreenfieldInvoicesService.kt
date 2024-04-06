package ua.com.radiokot.license.service.btcpay.greenfield.invocies

interface GreenfieldInvoicesService {
    /**
     * View information about the existing invoices.
     *
     * @param orderId Array of OrderIds to fetch the invoices for ([GreenfieldInvoice.Metadata.orderId])
     * @see <a href="https://docs.btcpayserver.org/API/Greenfield/v1/#operation/Invoices_GetInvoices">API reference</a>
     */
    fun getInvoices(orderId: Array<String>): List<GreenfieldInvoice>

    /**
     * Create a new invoice.
     *
     * @see <a href="https://docs.btcpayserver.org/API/Greenfield/v1/#operation/Invoices_CreateInvoice">API reference</a>
     */
    fun createInvoice(creationData: GreenfieldInvoiceCreationData): GreenfieldInvoice
}
