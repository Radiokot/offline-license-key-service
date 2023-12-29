package ua.com.radiokot.license.service.api.issuers

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import io.javalin.http.Context
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository
import java.net.HttpURLConnection

class IssuersController(
    private val issuersRepository: IssuersRepository,
    private val resourceConverter: ResourceConverter,
) {
    fun getIssuers(ctx: Context) = with(ctx) {
        val responseDocument = JSONAPIDocument(
            issuersRepository
                .getIssuers()
                .map(::IssuerResource)
        )

        status(HttpURLConnection.HTTP_OK)
        jsonApi(resourceConverter.writeDocumentCollection(responseDocument))
    }
}