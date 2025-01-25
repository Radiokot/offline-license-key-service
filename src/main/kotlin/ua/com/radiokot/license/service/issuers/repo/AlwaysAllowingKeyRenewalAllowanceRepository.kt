package ua.com.radiokot.license.service.issuers.repo

object AlwaysAllowingKeyRenewalAllowanceRepository : KeyRenewalAllowanceRepository {
    override fun getKeyRenewalAllowance(subject: String): Boolean = true
}
