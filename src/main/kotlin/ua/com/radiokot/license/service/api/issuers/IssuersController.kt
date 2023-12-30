package ua.com.radiokot.license.service.api.issuers

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import mu.KotlinLogging
import ua.com.radiokot.license.service.api.issuers.model.IssuerResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository

class IssuersController(
    private val issuersRepository: IssuersRepository,
    private val resourceConverter: ResourceConverter,
) {
    private val log = KotlinLogging.logger("IssuersController")

    fun getIssuers(ctx: Context) = with(ctx) {
        val responseDocument = JSONAPIDocument(
            issuersRepository
                .getIssuers()
                .map(::IssuerResource)
        )

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocumentCollection(responseDocument))
    }

    fun getIssuerById(ctx: Context) = with(ctx) {
        val issuerId = pathParam("issuerId")
        val issuer = issuersRepository.getIssuerById(issuerId)
            ?: throw NotFoundResponse("Issuer $issuerId not found")
                .also {
                    log.debug {
                        "getIssuerById(): issuer_not_found:" +
                                "\nissuerId=$issuerId"
                    }
                }

        val responseDocument = JSONAPIDocument(IssuerResource(issuer))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }
}