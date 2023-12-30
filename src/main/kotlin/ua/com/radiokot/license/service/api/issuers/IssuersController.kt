package ua.com.radiokot.license.service.api.issuers

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository

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

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocumentCollection(responseDocument))
    }
}