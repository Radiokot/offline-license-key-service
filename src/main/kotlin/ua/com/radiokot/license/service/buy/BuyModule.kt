package ua.com.radiokot.license.service.buy

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.features.featuresModule
import ua.com.radiokot.license.service.ioModule

val buyModule = module {
    includes(ioModule)
    includes(featuresModule)

    singleOf(::FormRedirectBuyController) bind FormRedirectBuyController::class
}
