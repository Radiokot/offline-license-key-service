package ua.com.radiokot.license.service.issuers.repo

import ua.com.radiokot.license.service.issuers.model.ConfiguredIssuer
import ua.com.radiokot.license.service.issuers.model.Issuer

class RealIssuersRepository(
    private val configuredIssuers: List<ConfiguredIssuer>,
) : IssuersRepository {
    private val decodedIssuers: List<Issuer> by lazy {
        configuredIssuers
            .map(::Issuer)
    }

    override fun getIssuers(): List<Issuer> = decodedIssuers
}