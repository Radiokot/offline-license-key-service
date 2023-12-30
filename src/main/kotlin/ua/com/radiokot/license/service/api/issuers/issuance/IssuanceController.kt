package ua.com.radiokot.license.service.api.issuers.issuance

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.exceptions.InvalidJsonApiResourceException
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import mu.KotlinLogging
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
    private val log = KotlinLogging.logger("IssuanceController")

    fun issueKey(ctx: Context) = with(ctx) {
        val issuanceRequest = try {
            resourceConverter
                .readDocument(bodyInputStream(), IssuanceRequestResource::class.java)
                .get()!!
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException,
                is InvalidJsonApiResourceException ->
                    throw BadRequestResponse(e.message ?: HttpStatus.BAD_REQUEST.message)
                        .also {
                            log.debug(e) {
                                "issueKey(): bad_request"
                            }
                        }

                else ->
                    throw e
            }
        }

        val issuerId = pathParam("issuerId")
        val issuer = issuersRepository.getIssuerById(issuerId)
            ?: throw NotFoundResponse("Issuer $issuerId not found")
                .also {
                    log.debug {
                        "issueKey(): issuer_not_found:" +
                                "\nissuerId=$issuerId"
                    }
                }

        val issuedKey = OfflineLicenseKeys.jwt
            .factory(
                issuerPrivateKey = issuer.privateKey as RSAPrivateKey,
                issuer = issuer.name,
            )
            .issue(
                subject = issuanceRequest.subject
                    ?: throw BadRequestResponse("The request must have subject string attribute"),
                hardware = issuanceRequest.hardware
                    ?: throw BadRequestResponse("The request must have hardware string attribute"),
                features = issuanceRequest.features?.toSet()
                    ?: throw BadRequestResponse("The request must have features array attribute"),
            )

        log.debug {
            "issueKey(): key_issued:" +
                    "\nissuer=${issuedKey.issuer}," +
                    "\nkey=${issuedKey.encode()}"
        }

        val responseDocument = JSONAPIDocument(IssuedKeyResource(issuedKey.encode()))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }
}