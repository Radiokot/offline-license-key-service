package ua.com.radiokot.license.service.payment

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.btcpay.btcPayModule
import ua.com.radiokot.license.service.extension.getNotEmptyProperty

val paymentMethodsModule = module {
    includes(btcPayModule)

    single {
        BtcPayPaymentMethodController(
            storeId = getNotEmptyProperty("BTCPAY_STORE_ID"),
            invoiceUrlFactory = { invoiceId ->
                getNotEmptyProperty("BTCPAY_INVOICE_BASE_URL").trimEnd('/') + "/$invoiceId"
            },
            ordersRepository = get(),
            greenfieldInvoicesApi = get(),
        )
    } bind BtcPayPaymentMethodController::class

    singleOf(::ManualCheckoutPaymentMethodController) bind ManualCheckoutPaymentMethodController::class
}
