package ua.com.radiokot.license.service.api.orders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import ua.com.radiokot.license.service.api.jsonapi.BaseResource;
import ua.com.radiokot.license.service.orders.Order;

import java.util.Locale;

@Type("orders")
public class OrderResource extends BaseResource {
    @JsonProperty("status")
    public Status status;

    @JsonProperty("absolute_url")
    public String absoluteUrl;

    public OrderResource(String id) {
        super(id);
    }

    public OrderResource(Order order) {
        super(order.getId());
        this.status = Status.values()[order.getStatus().ordinal()];
        this.absoluteUrl = order.getAbsoluteUrl();
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Status {
        PENDING,
        PAID,
        CLOSED,
        ;

        @JsonProperty("name")
        public String getName() {
            return this.toString().toLowerCase(Locale.ENGLISH);
        }

        @JsonProperty("value")
        public int getValue() {
            return ordinal();
        }
    }
}
