package ua.com.radiokot.license.service.jsonapi

import com.fasterxml.jackson.annotation.JsonProperty

class NameValueEnum(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("value")
    val value: Int
)