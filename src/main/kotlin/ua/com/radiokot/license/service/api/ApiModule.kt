package ua.com.radiokot.license.service.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jasminb.jsonapi.ResourceConverter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.api.features.FeaturesApiController
import ua.com.radiokot.license.service.api.features.model.FeatureResource
import ua.com.radiokot.license.service.api.issuers.IssuersApiController
import ua.com.radiokot.license.service.api.issuers.issuance.IssuanceApiController
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuanceRequestResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuedKeyResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.RenewalRequestResource
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource
import ua.com.radiokot.license.service.api.orders.OrdersApiController
import ua.com.radiokot.license.service.api.orders.model.OrderResource
import ua.com.radiokot.license.service.features.featuresModule
import ua.com.radiokot.license.service.ioModule
import ua.com.radiokot.license.service.issuers.di.issuersModule
import ua.com.radiokot.license.service.orders.ordersModule

val apiModule = module {
    includes(ioModule)
    single {
        ResourceConverter(
            get<ObjectMapper>(),
            IssuerResource::class.java,
            IssuanceRequestResource::class.java,
            RenewalRequestResource::class.java,
            IssuedKeyResource::class.java,
            OrderResource::class.java,
            FeatureResource::class.java,
        )
    } bind ResourceConverter::class

    includes(issuersModule)
    singleOf(::IssuersApiController)
    singleOf(::IssuanceApiController)

    includes(ordersModule)
    singleOf(::OrdersApiController)

    includes(featuresModule)
    singleOf(::FeaturesApiController)
}
