package ua.com.radiokot.license.service.jsonapi.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jasminb.jsonapi.ResourceConverter
import org.koin.dsl.module
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource

val jsonApiModule = module {
    single<ObjectMapper> {
        jacksonObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    single {
        ResourceConverter(
            get<ObjectMapper>(),
            IssuerResource::class.java,
        )
    }
}