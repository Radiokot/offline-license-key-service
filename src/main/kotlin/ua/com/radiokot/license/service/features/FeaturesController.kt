package ua.com.radiokot.license.service.features

import io.javalin.http.Context

class FeaturesController(
    private val featuresRepository: FeaturesRepository,
) {
    fun render(ctx: Context) = with(ctx) {
        render("features.html", mapOf(
            "features" to featuresRepository.getFeatures(),
        ))
    }
}
