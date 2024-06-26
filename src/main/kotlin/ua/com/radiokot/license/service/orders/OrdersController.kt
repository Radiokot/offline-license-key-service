package ua.com.radiokot.license.service.orders

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import okio.ByteString.Companion.encodeUtf8
import ua.com.radiokot.license.OfflineLicenseKeyFactory
import ua.com.radiokot.license.service.features.Feature
import ua.com.radiokot.license.service.features.FeaturesRepository
import ua.com.radiokot.license.service.turnstile.CloudflareTurnstile

typealias OrderCheckoutUrlFactory = (
    orderId: String,
    paymentMethodId: String,
) -> String

class OrdersController(
    private val orderCheckoutUrlFactory: OrderCheckoutUrlFactory,
    private val ordersRepository: OrdersRepository,
    private val featuresRepository: FeaturesRepository,
    private val keyFactory: OfflineLicenseKeyFactory,
    private val cloudflareTurnstile: CloudflareTurnstile?,
    private val keyActivationUri: String?,
) {
    fun getOrderById(ctx: Context) = with(ctx) {
        val orderId = pathParam("orderId")
        val order = ordersRepository.getOrderById(orderId)
            ?: throw NotFoundResponse("Order '$orderId' not found")

        when (order.status) {
            Order.Status.PENDING ->
                redirect(orderCheckoutUrlFactory(orderId, order.paymentMethodId))

            Order.Status.PAID ->
                render(
                    "successful_purchase.html", mapOf(
                        "encodedKey" to order.encodedKey,
                        "keyActivationUri" to keyActivationUri,
                    )
                )

            Order.Status.CLOSED ->
                result("This order is closed. Try ordering once again")
        }
    }

    fun createOrder(ctx: Context) = with(ctx) {
        val orderId = formParam("reference")
            ?.takeIf(String::isNotEmpty)
            ?.encodeUtf8()
            ?.sha256()
            ?.hex()
            ?: throw BadRequestResponse("Missing reference")

        if (cloudflareTurnstile != null) {
            val cloudflareTurnstileResponse = formParam(CloudflareTurnstile.BODY_FORM_RESPONSE)
                ?.takeIf(String::isNotEmpty)
                ?: throw BadRequestResponse("Missing Cloudflare Turnstile response")
            val cloudflareCallerIp = header(CloudflareTurnstile.HEADER_IP)

            if (!cloudflareTurnstile.verify(
                    response = cloudflareTurnstileResponse,
                    userIp = cloudflareCallerIp,
                    idempotencyKey = orderId,
                )
            ) {
                throw BadRequestResponse("Cloudflare Turnstile verification failed")
            }
        }

        val email = formParam("email")
            ?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing email")

        val hardware = formParam("hardware")
            ?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing hardware")

        val features = formParam("features")
            ?.takeIf(String::isNotEmpty)
            ?.split(',')
            ?.mapNotNullTo(mutableSetOf(), String::toIntOrNull)
            ?.takeIf(Collection<*>::isNotEmpty)
            ?.map { featureIndex ->
                featuresRepository[featureIndex]
                    ?: throw BadRequestResponse("Feature '$featureIndex' is unknown")
            }
            ?: throw BadRequestResponse("Missing features")

        val paymentMethod = queryParam("method")
            ?.takeIf(String::isNotEmpty)
            ?: throw BadRequestResponse("Missing payment method")

        val key = keyFactory.issue(
            subject = email,
            hardware = hardware,
            features = features.mapTo(mutableSetOf(), Feature::index),
        )

        val order = ordersRepository.getOrderById(orderId)
            ?: ordersRepository.createOrder(
                id = orderId,
                paymentMethodId = paymentMethod,
                amount = features.sumOf(Feature::price),
                currency = "USD",
                buyerEmail = email,
                encodedKey = key.encode(),
            )

        redirect(orderCheckoutUrlFactory(orderId, order.paymentMethodId))
    }
}
