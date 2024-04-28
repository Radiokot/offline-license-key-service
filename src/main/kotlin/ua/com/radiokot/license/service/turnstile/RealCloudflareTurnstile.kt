package ua.com.radiokot.license.service.turnstile

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class RealCloudflareTurnstile(
    override val siteKey: String,
    private val secretKey: String,
    private val httpClient: OkHttpClient,
    private val jsonObjectMapper: ObjectMapper,
) : CloudflareTurnstile {
    private val log = KotlinLogging.logger("RealCloudflareTurnstile")

    override fun verify(
        response: String,
        userIp: String?,
        idempotencyKey: String?,
    ): Boolean {
        val request = Request.Builder()
            .url("https://challenges.cloudflare.com/turnstile/v0/siteverify")
            .post(FormBody.Builder()
                .add("secret", secretKey)
                .add("response", response)
                .apply {
                    userIp?.also { add("remoteip", it) }
                    idempotencyKey?.also { add("idempotency_key", it) }
                }
                .build()
            )
            .build()

        return try {
            httpClient.newCall(request)
                .execute()
                .use { verificationResponse ->
                    jsonObjectMapper
                        .readTree(checkNotNull(verificationResponse.body?.byteStream()) {
                            "The response must have a body"
                        })
                        .get("success")
                        .asBoolean(false)
                }
        } catch (e: Exception) {
            log.error(e) {
                "verify(): verification_failed:" +
                        "\nresponse=$response," +
                        "\nuserIp=$userIp," +
                        "\nidempotencyKey=$idempotencyKey"
            }

            false
        }
    }
}
