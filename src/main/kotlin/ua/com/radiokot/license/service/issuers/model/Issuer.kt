package ua.com.radiokot.license.service.issuers.model

import mu.KotlinLogging
import ua.com.radiokot.license.service.util.RsaPemEncoding
import java.io.File
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey

class Issuer(
    val id: String,
    val name: String,
    val privateKey: PrivateKey,
    val publicKey: PublicKey,
) {
    constructor(configuredIssuer: ConfiguredIssuer) : this(
        id = configuredIssuer.id,
        name = configuredIssuer.name,
        privateKey = RsaPemEncoding()
            .decodePrivate(
                File(configuredIssuer.privateKeyPath)
                    .readText(Charsets.US_ASCII)
                    .also { privateKeyContent ->
                        log.debug {
                            "create(): private_key_read:" +
                                    "\nconfiguredIssuerId=${configuredIssuer.id}" +
                                    "\ncontent=$privateKeyContent"
                        }
                    }
            ),
        publicKey = RsaPemEncoding()
            .decodePublic(
                File(configuredIssuer.publicKeyPath)
                    .readText(Charsets.US_ASCII)
                    .also { publicKeyContent ->
                        log.debug {
                            "create(): public_key_read:" +
                                    "\nconfiguredIssuerId=${configuredIssuer.id}" +
                                    "\ncontent=$publicKeyContent"
                        }
                    }
            ),
    )

    fun encodePublicKey(): String =
        when (publicKey) {
            is RSAPublicKey ->
                RsaPemEncoding().encodePublic(publicKey)

            else ->
                error("Unknown public key type ${publicKey::class.java.name}")
        }

    private companion object {
        private val log = KotlinLogging.logger("Issuer")
    }
}