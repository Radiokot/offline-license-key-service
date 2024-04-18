package ua.com.radiokot.license.service.features

import java.math.BigDecimal

class HardcodedFeaturesRepository: FeaturesRepository {
    private val features = listOf(
        Feature(
            index = 1,
            name = "Test feature",
            description = "This is a feature reserved purely for testing purposes",
            bannerUrl = "https://feed.radiokot.com.ua/thumb/architecture.jpg",
            price = BigDecimal("100"),
        )
    )
    
    override fun getFeatures(): Collection<Feature> = features
}
