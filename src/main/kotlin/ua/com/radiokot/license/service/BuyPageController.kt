package ua.com.radiokot.license.service

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import ua.com.radiokot.license.service.features.FeaturesRepository

class BuyPageController(
    private val featuresRepository: FeaturesRepository,
) {
    fun render(ctx: Context) = with(ctx) {
        val featureIndex = queryParam("f")
            ?.takeIf(String::isNotEmpty)
            ?.toIntOrNull()
            ?: throw BadRequestResponse("Missing feature ID")

        val feature = featuresRepository[featureIndex]
            ?: throw NotFoundResponse("Feature '$featureIndex' not found")

        render("buy.html", mapOf(
            "feature" to feature,
        ))
    }
}
