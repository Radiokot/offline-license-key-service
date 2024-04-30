package ua.com.radiokot.license.service

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import ua.com.radiokot.license.service.features.FeaturesRepository
import ua.com.radiokot.license.service.turnstile.CloudflareTurnstile
import java.util.*

class BuyPageController(
    private val featuresRepository: FeaturesRepository,
    private val cloudflareTurnstile: CloudflareTurnstile?,
) {
    fun render(ctx: Context) = with(ctx) {
        val featureIndex = queryParam("f")
            ?.takeIf(String::isNotEmpty)
            ?.toIntOrNull()
            ?: throw BadRequestResponse("Missing feature ID")

        val feature = featuresRepository[featureIndex]
            ?: throw NotFoundResponse("Feature '$featureIndex' not found")

        val email = queryParam("email")
            ?.takeIf(String::isNotEmpty)

        val hardware = queryParam("hw")
            ?.takeIf(String::isNotEmpty)

        render("buy.html", mapOf(
            "feature" to feature,
            "reference" to UUID.randomUUID().toString(),
            "turnstileSiteKey" to cloudflareTurnstile?.siteKey,
            "email" to email,
            "hardware" to hardware,
        ))
    }
}
