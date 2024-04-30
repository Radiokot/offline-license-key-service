package ua.com.radiokot.license.service.api.features

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import ua.com.radiokot.license.service.api.features.model.FeatureResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.features.FeaturesRepository

class FeaturesApiController(
    private val featuresRepository: FeaturesRepository,
    private val resourceConverter: ResourceConverter,
) {
    fun getFeatures(ctx: Context) = with(ctx) {
        val responseDocument = JSONAPIDocument(
            featuresRepository
                .getFeatures()
                .map(::FeatureResource)
        )

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocumentCollection(responseDocument))
    }
}
