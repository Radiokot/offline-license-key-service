package ua.com.radiokot.license.service.util

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

class RsaPemEncoding {
    private val baseEncoder = Base64.getMimeEncoder(64, "\n".toByteArray(Charsets.US_ASCII))
    private val baseDecoder = Base64.getMimeDecoder()
    private val rsaKeyFactory = KeyFactory.getInstance("RSA")

    /**
     * @return X509 encoded key in PEM text format.
     */
    fun encodePublic(publicKey: RSAPublicKey): String =
        StringBuilder()
            .append(BEGIN_PUBLIC_KEY)
            .append('\n')
            .append(baseEncoder.encodeToString(publicKey.encoded))
            .append('\n')
            .append(END_PUBLIC_KEY)
            .toString()

    /**
     * @return PKCS#1 encoded key in PEM text format
     */
    fun encodePrivate(privateKey: RSAPrivateKey): String =
        StringBuilder()
            .append(BEGIN_RSA_PRIVATE_KEY)
            .append('\n')
            .append(baseEncoder.encodeToString(privateKey.encoded))
            .append('\n')
            .append(END_RSA_PRIVATE_KEY)
            .toString()

    /**
     * @param encodedPublicKey X509 encoded key in PEM text format
     */
    fun decodePublic(encodedPublicKey: String): RSAPublicKey =
        rsaKeyFactory
            .generatePublic(
                X509EncodedKeySpec(
                    baseDecoder.decode(
                        encodedPublicKey
                            .substringAfter(BEGIN_PUBLIC_KEY)
                            .substringBefore(END_PUBLIC_KEY)
                            .trimIndent()
                    )
                )
            )
                as RSAPublicKey

    /**
     * @param encodedPrivateKey PKCS#1 encoded key in PEM text format
     */
    fun decodePrivate(encodedPrivateKey: String): RSAPrivateKey =
        rsaKeyFactory
            .generatePrivate(
                PKCS8EncodedKeySpec(
                    baseDecoder.decode(
                        encodedPrivateKey
                            .substringAfter(BEGIN_RSA_PRIVATE_KEY)
                            .substringBefore(END_RSA_PRIVATE_KEY)
                            .trimIndent()
                    )
                )
            )
                as RSAPrivateKey

    companion object {
        private const val BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----"
        private const val END_PUBLIC_KEY = "-----END PUBLIC KEY-----"
        private const val BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----"
        private const val END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----"
    }
}
