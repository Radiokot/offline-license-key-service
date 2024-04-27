package ua.com.radiokot.license.service.api.jsonapi;

import com.github.jasminb.jsonapi.annotations.Id;

public class BaseResource {
    @Id
    public String id;

    public BaseResource(String id) {
        this.id = id;
    }
}
