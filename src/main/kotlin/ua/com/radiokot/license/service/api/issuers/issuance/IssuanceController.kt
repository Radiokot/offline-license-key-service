package ua.com.radiokot.license.service.api.issuers.issuance

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.exceptions.InvalidJsonApiResourceException
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import ua.com.radiokot.license.OfflineLicenseKeys
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuanceRequestResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuedKeyResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository
import java.security.interfaces.RSAPrivateKey

class IssuanceController(
    private val issuersRepository: IssuersRepository,
    private val resourceConverter: ResourceConverter,
) {
    fun issueKey(ctx: Context) = with(ctx) {
        val issuanceRequest = try {
            resourceConverter
                .readDocument(bodyInputStream(), IssuanceRequestResource::class.java)
                .get()!!
        } catch (jsonApiException: InvalidJsonApiResourceException) {
            throw BadRequestResponse(jsonApiException.message ?: HttpStatus.BAD_REQUEST.message)
        }

        val issuerId = pathParam("issuerId")
        val issuer = issuersRepository.getIssuerById(issuerId)
            ?: throw NotFoundResponse("Issuer $issuerId not found")

        val issuedKey = OfflineLicenseKeys.jwt
            .factory(
                issuerPrivateKey = issuer.privateKey as RSAPrivateKey,
                issuer = issuer.name,
            )
            .issue(
                subject = issuanceRequest.subject,
                hardware = issuanceRequest.hardware,
                features = issuanceRequest.features.toSet(),
            )
        val responseDocument = JSONAPIDocument(IssuedKeyResource(issuedKey.encode()))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }
}