package ua.com.radiokot.license.service.api.orders

import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import mu.KotlinLogging
import ua.com.radiokot.license.service.api.orders.model.OrderResource
import ua.com.radiokot.license.service.extension.jsonApi
import ua.com.radiokot.license.service.orders.OrdersRepository

class OrdersApiController(
    private val ordersRepository: OrdersRepository,
    private val resourceConverter: ResourceConverter,
) {
    private val log = KotlinLogging.logger("OrdersController")

    fun getOrderById(ctx: Context) = with(ctx) {
        val orderId = pathParam("orderId")
        val order = ordersRepository.getOrderById(orderId)
            ?: throw NotFoundResponse("Order '$orderId' not found")
                .also {
                    log.debug {
                        "getOrderById(): order_not_found:" +
                                "\norderId=$orderId"
                    }
                }

        val responseDocument = JSONAPIDocument(OrderResource(order))

        status(HttpStatus.OK)
        jsonApi(resourceConverter.writeDocument(responseDocument))
    }
}
