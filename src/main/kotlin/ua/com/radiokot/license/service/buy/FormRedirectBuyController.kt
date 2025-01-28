package ua.com.radiokot.license.service.buy

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import ua.com.radiokot.license.service.features.FeaturesRepository

class FormRedirectBuyController(
    private val featuresRepository: FeaturesRepository,
) {
    fun render(ctx: Context) = with(ctx) {
        val featureIndex = queryParam("f")
            ?.takeIf(String::isNotEmpty)
            ?.toIntOrNull()
            ?: throw BadRequestResponse("Missing feature index")

        val feature = featuresRepository[featureIndex]
            ?: throw NotFoundResponse(
                "Feature not found",
                mapOf(
                    "featureIndex" to featureIndex.toString(),
                )
            )

        val email = queryParam("email")
            ?.takeIf(String::isNotEmpty)

        val hardware = queryParam("hw")
            ?.takeIf(String::isNotEmpty)

        val redirectUrl =
            "https://docs.google.com/forms/d/e/1FAIpQLSeYrjdA4ANcY-AQqn9h0s6DhuGnf-kgFR8lEUPlvsmyxC3Piw/viewform"
                .toHttpUrl()
                .newBuilder()
                .addQueryParameter("entry.577174635", email)
                .addQueryParameter("entry.486638548", hardware)
                .addQueryParameter("entry.1448169026", feature.name)
                .toString()

        redirect(redirectUrl)
    }
}
