package ua.com.radiokot.license.service

import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.environmentProperties
import ua.com.radiokot.license.service.di.KLoggerKoinLogger

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
    }
}