package ua.com.radiokot.license.service.features

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val featuresModule = module {
    singleOf(::HardcodedFeaturesRepository) bind FeaturesRepository::class
}
