package ua.com.radiokot.license.service.issuers.repo

import ua.com.radiokot.license.service.issuers.model.Issuer

interface IssuersRepository {
    fun getIssuers(): List<Issuer>
}