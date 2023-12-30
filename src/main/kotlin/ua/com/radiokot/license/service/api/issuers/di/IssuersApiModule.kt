package ua.com.radiokot.license.service.api.issuers.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ua.com.radiokot.license.service.api.issuers.IssuersController
import ua.com.radiokot.license.service.api.issuers.issuance.IssuanceController
import ua.com.radiokot.license.service.issuers.di.issuersModule
import ua.com.radiokot.license.service.jsonapi.di.jsonApiModule

val issuersApiModule = module {
    includes(issuersModule)
    includes(jsonApiModule)

    singleOf(::IssuersController)
    singleOf(::IssuanceController)
}