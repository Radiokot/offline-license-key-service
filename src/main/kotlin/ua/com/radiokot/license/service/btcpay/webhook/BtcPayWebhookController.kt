package ua.com.radiokot.license.service.btcpay.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import mu.KotlinLogging
import ua.com.radiokot.license.service.orders.BtcPayOrdersRepository
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class BtcPayWebhookController(
    webhookSecret: String,
    private val storeId: String,
    private val btcPayOrdersRepository: BtcPayOrdersRepository,
    private val jsonObjectMapper: ObjectMapper,
) {
    private val log = KotlinLogging.logger("BtcPayWebhookController")
    private val hmacKey = SecretKeySpec(webhookSecret.encodeToByteArray(), "HmacSHA256")

    fun handleEvent(ctx: Context) = with(ctx) {
        val expectedDigest = header("BTCPay-Sig")
            ?.substringAfter("sha256=")
            ?: throw BadRequestResponse("Missing 'BTCPay-Sig' header")
        val rawBody = bodyAsBytes()
        val bodyDigest = rawBody.hmacSha256()

        if (expectedDigest != bodyDigest) {
            throw BadRequestResponse(
                "Body digest mismatch",
                mapOf(
                    "expected" to expectedDigest,
                    "actual" to bodyDigest,
                )
            )
        }

        val event: BtcPayWebhookEvent = try {
            jsonObjectMapper.readValue(rawBody, BtcPayWebhookEvent::class.java)
        } catch (e: Exception) {
            throw BadRequestResponse(
                "Failed to parse the body",
                mapOf(
                    "exception" to e.toString(),
                )
            )
        }

        if (storeId != event.storeId) {
            throw BadRequestResponse(
                "The event does not belong to the configured store",
                mapOf(
                    "storeId" to storeId,
                )
            )
        }

        log.debug {
            "handleEvent(): received_valid_event:" +
                    "\ntype=${event.type}"
        }

        json(
            mapOf(
                "code" to 200,
                "message" to "OK",
            )
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun ByteArray.hmacSha256(): String =
        Mac.getInstance("HmacSHA256").run {
            init(hmacKey)
            update(this@hmacSha256)
            doFinal().toHexString()
        }
}
