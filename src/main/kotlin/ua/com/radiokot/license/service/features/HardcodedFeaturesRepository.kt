package ua.com.radiokot.license.service.features

import java.math.BigDecimal

class HardcodedFeaturesRepository : FeaturesRepository {
    private val features = listOf(
        Feature(
            index = 0,
            name = "Memories",
            description = "Get a daily collection of photos and videos from the same day in past years",
            bannerUrl = "https://github.com/user-attachments/assets/1287df09-3d76-450d-b411-d883458f80cb",
            pageUrl = "https://github.com/Radiokot/photoprism-android-client/wiki/Memories-extension",
            price = BigDecimal("2"),
        ),
        Feature(
            index = 2,
            name = "Photo frame widget",
            description = "See random photos from your library on the home screen",
            bannerUrl = "https://github.com/user-attachments/assets/32d4d3c1-dae1-4368-bbe7-625c833525f0",
            pageUrl = "https://github.com/Radiokot/photoprism-android-client/wiki/Photo-frame-widget-extension",
            price = BigDecimal("2"),
        )
    )

    override fun getFeatures(): Collection<Feature> = features
}
