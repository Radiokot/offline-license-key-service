package ua.com.radiokot.license.service.api.issuers.issuance

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.exceptions.InvalidJsonApiResourceException
import io.javalin.http.*
import mu.KotlinLogging
import ua.com.radiokot.license.OfflineLicenseKey
import ua.com.radiokot.license.OfflineLicenseKeyVerificationException
import ua.com.radiokot.license.OfflineLicenseKeys
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuanceRequestResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.IssuedKeyResource
import ua.com.radiokot.license.service.api.issuers.issuance.model.RenewalRequestResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.issuers.repo.IssuersRepository
import ua.com.radiokot.license.service.issuers.repo.KeyRenewalAllowanceRepository
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class IssuanceApiController(
    private val issuersRepository: IssuersRepository,
    private val renewalAllowanceRepository: KeyRenewalAllowanceRepository,
    private val resourceConverter: ResourceConverter,
) {
    private val log = KotlinLogging.logger("IssuanceController")

    fun issueKey(ctx: Context) = with(ctx) {
        val issuanceRequest: IssuanceRequestResource
        val issuanceRequestMeta: Map<String, Any?>?

        try {
            val requestDocument = resourceConverter
                .readDocument(bodyInputStream(), IssuanceRequestResource::class.java)

            issuanceRequest = requestDocument.get()!!
            issuanceRequestMeta = requestDocument.meta
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

        log.info {
            "issueKey(): key_issued:" +
                    "\nissuer=${issuedKey.issuer}," +
                    "\nsubject=${issuedKey.subject}," +
                    "\nrequestMeta=${issuanceRequestMeta}," +
                    "\nkey=${issuedKey.encode()}"
        }

        val responseDocument = JSONAPIDocument(IssuedKeyResource(issuedKey.encode()))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }

    fun renewKey(ctx: Context) = with(ctx) {
        val renewalRequest: RenewalRequestResource = try {
            resourceConverter
                .readDocument(bodyInputStream(), RenewalRequestResource::class.java)
                .get()!!
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException,
                is InvalidJsonApiResourceException ->
                    throw BadRequestResponse(
                        "Failed to parse the request",
                        mapOf(
                            "exception" to e.toString(),
                        )
                    )

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

        val keyToRenew: OfflineLicenseKey = try {
            OfflineLicenseKeys.jwt
                .verifyingReader(
                    issuerPublicKey = issuer.publicKey as RSAPublicKey,
                    issuer = issuer.name,
                )
                .read(
                    encoded = renewalRequest.key
                        ?: throw BadRequestResponse("The request must have key string attribute"),
                )
        } catch (e: Exception) {
            when (e) {
                is OfflineLicenseKeyVerificationException,
                is IllegalArgumentException ->
                    throw throw BadRequestResponse(
                        "Failed to parse the key",
                        mapOf(
                            "exception" to e.toString(),
                        )
                    )

                else ->
                    throw e
            }
        }

        val renewedKey = OfflineLicenseKeys.jwt
            .factory(
                issuerPrivateKey = issuer.privateKey as RSAPrivateKey,
                issuer = issuer.name,
            )
            .copy(
                source = keyToRenew,
                hardware = renewalRequest.hardware
                    ?: throw BadRequestResponse("The request must have hardware string attribute"),
            )

        if (!renewalAllowanceRepository.getKeyRenewalAllowance(renewedKey.subject)) {
            throw ForbiddenResponse(
                "The renewal for this key subject is not currently allowed",
            )
                .also {
                    log.debug {
                        "renewKey(): renewal_not_allowed:" +
                                "\nsubject=${renewedKey.subject}"
                    }
                }
        }

        log.info {
            "renewKey(): new_key_issued:" +
                    "\nissuer=${renewedKey.issuer}," +
                    "\nsubject=${renewedKey.subject}," +
                    "\nhardware=${renewedKey.hardware}," +
                    "\nkey=${renewedKey.encode()}"
        }

        val responseDocument = JSONAPIDocument(IssuedKeyResource(renewedKey.encode()))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }
}
