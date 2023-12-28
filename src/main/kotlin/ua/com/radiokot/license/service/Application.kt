package ua.com.radiokot.license.service

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.http.Header
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.environmentProperties
import sun.misc.Signal
import ua.com.radiokot.license.service.di.KLoggerKoinLogger
import ua.com.radiokot.license.service.util.JavalinResponseStatusLogger

object Application : KoinComponent {
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
        }

        Javalin
            .create { config ->
                config.showJavalinBanner = false
                config.requestLogger.http(JavalinResponseStatusLogger())
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