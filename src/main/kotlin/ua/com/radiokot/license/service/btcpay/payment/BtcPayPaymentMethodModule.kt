package ua.com.radiokot.license.service.btcpay.payment

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.btcpay.greenfield.greenfieldModule
import ua.com.radiokot.license.service.extension.getNotEmptyProperty

val btcPayPaymentMethodModule = module {
    includes(greenfieldModule)

    single {
        BtcPayPaymentMethodController(
            storeId = getNotEmptyProperty("BTCPAY_STORE_ID"),
            invoiceUrlFactory = { invoiceId ->
                getNotEmptyProperty("BTCPAY_INVOICE_BASE_URL")
                    .toHttpUrl()
                    .newBuilder()
                    .addPathSegment(invoiceId)
                    .build()
                    .toString()
            },
            ordersRepository = get(),
            greenfieldInvoicesApi = get(),
        )
    } bind BtcPayPaymentMethodController::class

}
