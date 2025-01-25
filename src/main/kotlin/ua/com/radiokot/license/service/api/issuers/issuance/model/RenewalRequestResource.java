package ua.com.radiokot.license.service.api.issuers.issuance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import ua.com.radiokot.license.service.api.jsonapi.BaseResource;

@Type("renewal-requests")
public class RenewalRequestResource extends BaseResource {
    @JsonProperty("key")
    public String key;

    @JsonProperty("hardware")
    public String hardware;

    public RenewalRequestResource(String id) {
        super(id);
    }

    // Jackson constructor.
    public RenewalRequestResource() {
        this("0");
    }
}
