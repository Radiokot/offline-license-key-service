package ua.com.radiokot.license.service.issuers.repo

import java.time.Duration
import java.time.Instant

class InMemoryTimeoutKeyRenewalAllowanceRepository(
    private val renewalTimeout: Duration,
) : KeyRenewalAllowanceRepository {
    private val instantsBySubject = mutableMapOf<String, Instant>()

    override fun getKeyRenewalAllowance(
        subject: String,
    ): Boolean = synchronized(this) {
        val now = Instant.now()
        val existingInstant = instantsBySubject[subject]

        return@synchronized if (existingInstant != null && existingInstant + renewalTimeout > now) {
            false
        } else {
            instantsBySubject[subject] = now
            true
        }
    }
}
