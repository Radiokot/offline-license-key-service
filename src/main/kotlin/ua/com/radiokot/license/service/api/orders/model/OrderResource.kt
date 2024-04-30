package ua.com.radiokot.license.service.api.orders.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Type
import ua.com.radiokot.license.service.api.jsonapi.BaseResource
import ua.com.radiokot.license.service.orders.Order

@Type("orders")
class OrderResource
@JsonCreator
constructor(
    id: String?,
    @JsonProperty("absolute_url")
    val absoluteUrl: String,
    @JsonProperty("status")
    val status: Status,
) : BaseResource(id) {
    constructor(order: Order) : this(
        id = order.id,
        absoluteUrl = order.absoluteUrl,
        status = Status.values()[order.status.ordinal],
    )

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    enum class Status {
        PENDING,
        PAID,
        CLOSED,
        ;

        @JsonProperty("name")
        fun getName(): String =
            this.toString().lowercase()

        @JsonProperty("value")
        fun getValue(): Int =
            ordinal
    }
}
