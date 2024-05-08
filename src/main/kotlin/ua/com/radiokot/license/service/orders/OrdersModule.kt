package ua.com.radiokot.license.service.orders

import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ua.com.radiokot.license.OfflineLicenseKeys
import ua.com.radiokot.license.service.btcpay.btcPayModule
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.model.GreenfieldInvoice
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.features.featuresModule
import ua.com.radiokot.license.service.ioModule
import ua.com.radiokot.license.service.issuers.di.issuersModule
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository
import ua.com.radiokot.license.service.orders.notifications.MailjetOrderNotificationsManager
import ua.com.radiokot.license.service.orders.notifications.OrderNotificationsManager
import java.security.interfaces.RSAPrivateKey

val ordersModule = module {
    includes(
        ioModule,
        btcPayModule,
        featuresModule,
        issuersModule,
    )

    single {
        OrderAbsoluteUrlFactory { orderId ->
            getNotEmptyProperty("PUBLIC_BASE_URL").trimEnd('/') +
                    "/orders/$orderId"
        }
    } bind OrderAbsoluteUrlFactory::class

    single {
        BtcPayOrdersRepository(
            storeId = getNotEmptyProperty("BTCPAY_STORE_ID"),
            orderAbsoluteUrlFactory = get(),
            speedPolicy = GreenfieldInvoice.SpeedPolicy.HIGH,
            greenfieldInvoicesApi = get(),
            jsonObjectMapper = get(),
        )
    } bind OrdersRepository::class

    single {
        DummyOrdersRepository(
            orderAbsoluteUrlFactory = get(),
        )
    } bind OrdersRepository::class

    if (System.getenv("MAILJET_ORDER_NOTIFICATIONS") != null) {
        factory {
            MailjetOrderNotificationsManager(
                senderEmail = getNotEmptyProperty("MAILJET_SENDER_EMAIL"),
                senderName = getNotEmptyProperty("MAILJET_SENDER_NAME"),
                pendingOrderTemplateId = getNotEmptyProperty("MAILJET_PENDING_ORDER_TEMPLATE_ID").toLong(),
                paidOrderTemplateId = getNotEmptyProperty("MAILJET_PAID_ORDER_TEMPLATE_ID").toLong(),
                apiKey = getNotEmptyProperty("MAILJET_API_KEY"),
                apiSecretKey = getNotEmptyProperty("MAILJET_API_SECRET_KEY"),
                httpClient = get(),
            )
        } binds arrayOf(OrderNotificationsManager::class)
    }

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
                },
            cloudflareTurnstile = getOrNull(),
            keyActivationUri = getPropertyOrNull("KEY_ACTIVATION_URI")
        )
    } bind OrdersController::class
}
