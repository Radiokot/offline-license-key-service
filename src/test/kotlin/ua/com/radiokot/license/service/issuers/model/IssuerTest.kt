package ua.com.radiokot.license.service.issuers.model

import org.junit.Assert
import org.junit.Test
import java.io.File


class IssuerTest {
    @Test
    fun createSuccessfully_IfUsingConfiguredIssuer() {
        val configuredIssuer = ConfiguredIssuer(
            id = "0",
            name = "test@radiokot.com.ua",
            privateKeyPath = "./test_private_key.pem",
            publicKeyPath = "./test_public_key.pem",
        )

        val createdIssuer = Issuer(configuredIssuer)

        Assert.assertEquals(
            configuredIssuer.id,
            createdIssuer.id
        )
        Assert.assertEquals(
            configuredIssuer.name,
            createdIssuer.name,
        )
        Assert.assertEquals(
            File(configuredIssuer.publicKeyPath)
                .readText(Charsets.US_ASCII)
                .trimIndent(),
            createdIssuer.encodePublicKey(),
        )
    }
}