package ua.com.radiokot.license.service.orders

import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.OfflineLicenseKeys
import ua.com.radiokot.license.service.btcpay.greenfield.greenfieldModule
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.features.featuresModule
import ua.com.radiokot.license.service.issuers.di.issuersModule
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository
import ua.com.radiokot.license.service.jsonapi.di.jsonApiModule
import java.security.interfaces.RSAPrivateKey

val ordersModule = module {
    includes(
        greenfieldModule,
        jsonApiModule,
        featuresModule,
        issuersModule,
    )

    single {
        BtcPayOrdersRepository(
            storeId = getNotEmptyProperty("BTCPAY_STORE_ID"),
            absoluteOrderUrlFactory = { orderId ->
                getNotEmptyProperty("PUBLIC_BASE_URL").trimEnd('/') +
                        "/orders/$orderId"
            },
            speedPolicy = GreenfieldInvoice.SpeedPolicy.HIGH,
            greenfieldInvoicesApi = get(),
            jsonObjectMapper = get(),
        )
    } bind OrdersRepository::class

    single {
        OrdersController(
            orderCheckoutUrlFactory = { orderId, paymentMethodId ->
                "/checkout/$paymentMethodId/$orderId"
            },
            ordersRepository = get(),
            featuresRepository = get(),
            keyFactory = get<IssuersRepository>()
                .getIssuerById("0")!!
                .let { issuer ->
                    OfflineLicenseKeys.jwt.factory(
                        issuerPrivateKey = issuer.privateKey as RSAPrivateKey,
                        issuer = issuer.name,
                    )
                }
        )
    } bind OrdersController::class
}
