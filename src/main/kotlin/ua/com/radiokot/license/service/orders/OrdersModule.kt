package ua.com.radiokot.license.service.orders

import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.btcpay.greenfield.greenfieldModule
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.jsonapi.di.jsonApiModule

val ordersModule = module {
    includes(
        greenfieldModule,
        jsonApiModule,
    )

    single {
        BtcPayOrdersRepository(
            orderUrlFactory = { orderId ->
                "/orders/$orderId"
            },
            speedPolicy = GreenfieldInvoice.SpeedPolicy.HIGH,
            greenfieldStoreInvoicesService = get(),
            jsonObjectMapper = get(),
        )
    } bind OrdersRepository::class

    single {
        OrdersController(
            orderCheckoutUrlFactory = { orderId, paymentMethodId ->
                "/checkout/$paymentMethodId/$orderId"
            },
            ordersRepository = get(),
        )
    } bind OrdersController::class
}