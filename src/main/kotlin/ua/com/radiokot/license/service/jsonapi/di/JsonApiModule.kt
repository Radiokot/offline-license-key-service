package ua.com.radiokot.license.service.jsonapi.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jasminb.jsonapi.ResourceConverter
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuanceRequestResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuedKeyResource
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource

val jsonApiModule = module {
    single {
        jacksonObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    } bind ObjectMapper::class

    single {
        ResourceConverter(
            get<ObjectMapper>(),
            IssuerResource::class.java,
            IssuanceRequestResource::class.java,
            IssuedKeyResource::class.java,
        )
    } bind ResourceConverter::class
}