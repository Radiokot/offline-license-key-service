package ua.com.radiokot.license.service.issuers.repo

interface KeyRenewalAllowanceRepository {

    /**
     * @param subject requester of the renewal
     *
     * @return true if the renewal is currently allowed for the [subject]
     */
    fun getKeyRenewalAllowance(
        subject: String,
    ): Boolean
}
