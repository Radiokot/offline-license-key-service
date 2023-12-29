package ua.com.radiokot.license.service.issuers.model

class ConfiguredIssuer(
    val id: String,
    val name: String,
    val privateKeyPath: String,
    val publicKeyPath: String,
)