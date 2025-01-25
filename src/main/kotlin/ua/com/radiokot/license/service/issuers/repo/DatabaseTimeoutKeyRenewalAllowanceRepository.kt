package ua.com.radiokot.license.service.issuers.repo

import java.sql.Connection
import java.time.Duration
import java.time.Instant

class DatabaseTimeoutKeyRenewalAllowanceRepository(
    private val dbConnection: Connection,
    private val renewalTimeout: Duration,
) : KeyRenewalAllowanceRepository {

    override fun getKeyRenewalAllowance(subject: String): Boolean = synchronized(this) {
        createTableIfNotExists()

        val now = Instant.now()
        val existingInstant = getExistingRenewalInstant(subject)

        return@synchronized if (existingInstant != null && existingInstant + renewalTimeout > now) {
            false
        } else {
            createOrUpdateRenewal(
                subject = subject,
                instant = now,
            )
            true
        }
    }

    private var isTableCreated = false
    private fun createTableIfNotExists() {
        if (isTableCreated) {
            return
        }

        dbConnection.createStatement()
            .execute(
                """
                CREATE TABLE IF NOT EXISTS key_renewals (
                    subject TEXT PRIMARY KEY, 
                    allowed_at_ms INTEGER NOT NULL
                )
                """.trimIndent()
            )

        isTableCreated = true
    }

    private fun getExistingRenewalInstant(subject: String): Instant? {
        val result = dbConnection
            .prepareStatement(
                """
                SELECT allowed_at_ms FROM key_renewals WHERE subject = ?
                """.trimIndent()
            )
            .apply { setString(1, subject) }
            .executeQuery()

        return if (result.next()) {
            Instant.ofEpochMilli(result.getLong(1))
        } else {
            null
        }
    }

    private fun createOrUpdateRenewal(
        subject: String,
        instant: Instant,
    ) {
        dbConnection
            .prepareStatement(
                """
                INSERT OR REPLACE INTO key_renewals
                (subject, allowed_at_ms)
                VALUES (?, ?)
                """.trimIndent()
            )
            .apply {
                setString(1, subject)
                setLong(2, instant.toEpochMilli())
            }
            .execute()
    }
}
