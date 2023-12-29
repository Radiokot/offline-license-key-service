package ua.com.radiokot.license.service.api.issuers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import ua.com.radiokot.license.service.issuers.model.Issuer;
import ua.com.radiokot.license.service.jsonapi.BaseResource;

@Type("issuers")
public class IssuerResource extends BaseResource {
    @JsonProperty("name")
    public String name;

    @JsonProperty("x509_public_key")
    public String x509PublicKey;

    public IssuerResource(String id) {
        super(id);
    }

    public IssuerResource(Issuer issuer) {
        super(issuer.getId());
        this.name = issuer.getName();
        this.x509PublicKey = issuer.encodePublicKey();
    }
}
