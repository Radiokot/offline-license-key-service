package ua.com.radiokot.license.service.orders.notifications

import com.mailjet.client.ClientOptions
import com.mailjet.client.MailjetClient
import com.mailjet.client.transactional.*
import com.mailjet.client.transactional.response.SentMessageStatus
import mu.KotlinLogging
import okhttp3.OkHttpClient
import ua.com.radiokot.license.service.orders.Order

class MailjetOrderNotificationsManager(
    senderEmail: String,
    senderName: String,
    private val pendingOrderTemplateId: Long,
    private val paidOrderTemplateId: Long,
    apiKey: String,
    apiSecretKey: String,
    httpClient: OkHttpClient,
) : OrderNotificationsManager {
    private val log = KotlinLogging.logger("MailjetOrderNotificationsManager")

    private val mailjetClient: MailjetClient by lazy {
        MailjetClient(
            ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecretKey)
                .okHttpClient(httpClient)
                .build()
        )
    }
    private val sender = SendContact(senderEmail, senderName)

    override fun notifyBuyerOfOrder(order: Order) {
        log.debug {
            "notifyBuyerOfOrder(): notifying:" +
                    "\nbuyerEmail=${order.buyerEmail}," +
                    "\norderStatus=${order.status}"
        }

        val email = when (order.status) {
            Order.Status.PENDING ->
                getPendingOrderEmail(order)

            Order.Status.PAID ->
                getPaidOrderEmail(order)

            Order.Status.CLOSED ->
                TODO()
        }

        val sendEmailsRequest = SendEmailsRequest
            .builder()
            .message(email)
            .build()

        try {
            val sendEmailResponse = sendEmailsRequest.sendWith(mailjetClient).messages[0]

            if (sendEmailResponse.status == SentMessageStatus.SUCCESS) {
                log.debug {
                    "notifyBuyerOfOrder(): notified_successfully:" +
                            "\nbuyerEmail=${order.buyerEmail}," +
                            "\norderStatus=${order.status}"
                }
            } else {
                log.error {
                    "notifyBuyerOfOrder(): notification_error_occurred:" +
                            "\nbuyerEmail=${order.buyerEmail}," +
                            "\norderStatus=${order.status}," +
                            "\nsendEmailErrors=${sendEmailResponse.errors}"
                }
            }
        } catch (e: Throwable) {
            log.error(e) {
                "failed_sending_notification"
            }
        }
    }

    private fun getPendingOrderEmail(order: Order): TransactionalEmail =
        newTransactionalEmail()
            .to(SendContact(order.buyerEmail))
            .subject("Order for gallery extension")
            .templateID(pendingOrderTemplateId)
            .variable("ORDER_URL", order.absoluteUrl)
            .build()

    private fun getPaidOrderEmail(order: Order): TransactionalEmail =
        newTransactionalEmail()
            .to(SendContact(order.buyerEmail))
            .subject("Your gallery extension key")
            .templateID(paidOrderTemplateId)
            .variable("ENCODED_KEY", order.encodedKey)
            .build()

    private fun newTransactionalEmail(): TransactionalEmail.TransactionalEmailBuilder =
        TransactionalEmail
            .builder()
            .from(sender)
            .templateLanguage(true)
            .trackClicks(TrackClicks.DISABLED)
            .trackOpens(TrackOpens.DISABLED)
}
