package ua.com.radiokot.license.service.api.issuers.issuance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import ua.com.radiokot.license.service.api.jsonapi.BaseResource;

@Type("issued-keys")
public class IssuedKeyResource extends BaseResource {
    @JsonProperty("key")
    String key;

    public IssuedKeyResource(String key) {
        super(String.valueOf(Math.abs(key.hashCode())));
        this.key = key;
    }
}
