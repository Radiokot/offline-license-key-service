package ua.com.radiokot.license.service

import io.javalin.http.Context
import ua.com.radiokot.license.service.payment.BtcPayPaymentMethodController
import ua.com.radiokot.license.service.payment.ManualCheckoutPaymentMethodController

class TestPageController {
    fun render(ctx: Context) = with(ctx) {
        render(
            "test.html", mapOf(
                "price" to "10 USD",
                "createBtcPayOrderUrl" to "/orders?method=${BtcPayPaymentMethodController.PAYMENT_METHOD_ID}",
                "createManualCheckoutOrderUrl" to "/orders?method=${ManualCheckoutPaymentMethodController.PAYMENT_METHOD_ID}",
            )
        )
    }
}
