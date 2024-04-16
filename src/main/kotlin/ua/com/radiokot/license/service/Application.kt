package ua.com.radiokot.license.service

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import io.javalin.http.Header
import io.javalin.http.servlet.HttpResponseExceptionMapper
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.environmentProperties
import sun.misc.Signal
import ua.com.radiokot.license.service.api.issuers.IssuersController
import ua.com.radiokot.license.service.api.issuers.di.issuersApiModule
import ua.com.radiokot.license.service.api.issuers.issuance.IssuanceController
import ua.com.radiokot.license.service.orders.OrdersController
import ua.com.radiokot.license.service.orders.ordersModule
import ua.com.radiokot.license.service.payment.BtcPayPaymentMethodController
import ua.com.radiokot.license.service.payment.ManualCheckoutPaymentMethodController
import ua.com.radiokot.license.service.payment.paymentMethodsModule
import ua.com.radiokot.license.service.util.JavalinResponseStatusLogger
import ua.com.radiokot.license.service.util.KLoggerKoinLogger

object Application : KoinComponent {
    private val apiLog = KotlinLogging.logger("API")

    @JvmStatic
    fun main(args: Array<String>) {
        startKoin {
            logger(
                KLoggerKoinLogger(
                    kLogger = KotlinLogging.logger("Koin"),
                    level = Level.DEBUG,
                )
            )

            environmentProperties()

            modules(
                issuersApiModule,
                paymentMethodsModule,
                ordersModule,
            )
        }

        Javalin
            .create { config ->
                config.showJavalinBanner = false
                config.requestLogger.http(
                    JavalinResponseStatusLogger(
                        kLogger = apiLog,
                    )
                )
            }
            .after { ctx ->
                ctx.header(Header.SERVER, "olk-svc")
            }
            .routes {
                path("/") {
                    get { ctx ->
                        ctx.result("Hi there")
                    }
                }

                path("v1/") {
                    get(
                        "issuers",
                        get<IssuersController>()::getIssuers
                    )

                    get(
                        "issuers/{issuerId}",
                        get<IssuersController>()::getIssuerById
                    )

                    post(
                        "issuers/{issuerId}/issuance",
                        get<IssuanceController>()::issueKey
                    )
                }

                path("orders/") {
                    get(
                        "{orderId}",
                        get<OrdersController>()::getOrderById
                    )
                }

                get(
                    "checkout/${BtcPayPaymentMethodController.PAYMENT_METHOD_ID}/{orderId}",
                    get<BtcPayPaymentMethodController>()::checkout,
                )

                get(
                    "checkout/${ManualCheckoutPaymentMethodController.PAYMENT_METHOD_ID}/{orderId}",
                    get<ManualCheckoutPaymentMethodController>()::checkout,
                )
            }
            .exception(BadRequestResponse::class.java) { e, ctx ->
                apiLog.debug(e) {
                    "bad_request"
                }
                HttpResponseExceptionMapper.handle(e, ctx)
            }
            .start(getKoin().getProperty("PORT", "8041").toInt())
            .apply {
                // Gracefully stop on SIGINT and SIGTERM.
                listOf("INT", "TERM").forEach {
                    Signal.handle(Signal(it)) { stop() }
                }
            }
    }
}
