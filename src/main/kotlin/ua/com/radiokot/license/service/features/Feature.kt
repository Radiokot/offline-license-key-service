package ua.com.radiokot.license.service.features

import java.math.BigDecimal

data class Feature(
    val index: Int,
    val name: String,
    val description: String,
    val bannerUrl: String,
    val price: BigDecimal,
)
