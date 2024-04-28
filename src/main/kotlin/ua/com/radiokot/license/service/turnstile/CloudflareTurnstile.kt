package ua.com.radiokot.license.service.turnstile

interface CloudflareTurnstile {
    val siteKey: String

    fun verify(
        response: String,
        userIp: String?,
        idempotencyKey: String?
    ): Boolean

    companion object {
        const val HEADER_IP = "CF-Connecting-IP"
        const val BODY_FORM_RESPONSE = "cf-turnstile-response"
    }
}
