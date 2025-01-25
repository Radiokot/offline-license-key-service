package ua.com.radiokot.license.service.api.features.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Type
import ua.com.radiokot.license.service.api.jsonapi.BaseResource
import ua.com.radiokot.license.service.features.Feature

@Type("features")
class FeatureResource
@JsonCreator
constructor(
    id: String?,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("price")
    val price: String,
    @JsonProperty("page")
    val page: String,
) : BaseResource(id) {

    constructor(feature: Feature) : this(
        id = feature.index.toString(),
        name = feature.name,
        price = feature.price.toPlainString(),
        page = feature.pageUrl,
    )
}
