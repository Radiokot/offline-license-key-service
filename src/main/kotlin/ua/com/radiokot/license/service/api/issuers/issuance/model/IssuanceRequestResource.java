package ua.com.radiokot.license.service.api.issuers.issuance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import ua.com.radiokot.license.service.jsonapi.BaseResource;

import java.util.List;

@Type("issuance-requests")
public class IssuanceRequestResource extends BaseResource {
    @JsonProperty("subject")
    public String subject;

    @JsonProperty("hardware")
    public String hardware;

    @JsonProperty("features")
    public List<Integer> features;

    public IssuanceRequestResource(String id) {
        super(id);
    }

    // Jackson constructor.
    public IssuanceRequestResource() {
        this("0");
    }
}
