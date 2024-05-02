package ua.com.radiokot.license.service.features

import java.math.BigDecimal

class HardcodedFeaturesRepository: FeaturesRepository {
    private val features = listOf(
        Feature(
            index = 0,
            name = "The feature",
            description = "This is a feature reserved purely for testing purposes",
            bannerUrl = "https://u.macula.link/1V-yMKxYTCmo7kXZ6pM-Lg-7",
            price = BigDecimal("2"),
        ),
        Feature(
            index = 2,
            name = "Yet another extension",
            description = "This is a feature reserved purely for testing purposes",
            bannerUrl = "https://u.macula.link/vgJasPbpRNi57GTm_JGIgA-7",
            price = BigDecimal("25"),
        )
    )
    
    override fun getFeatures(): Collection<Feature> = features
}
