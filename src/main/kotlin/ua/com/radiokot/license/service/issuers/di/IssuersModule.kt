package ua.com.radiokot.license.service.issuers.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.issuers.model.ConfiguredIssuer
import ua.com.radiokot.license.service.issuers.repo.*
import java.time.Duration

val issuersModule = module {
    single {
        val theOnlyConfiguredIssuer = ConfiguredIssuer(
            id = "0",
            name = getNotEmptyProperty("ISSUER_NAME"),
            privateKeyPath = getNotEmptyProperty("ISSUER_PRIVATE_KEY"),
            getNotEmptyProperty("ISSUER_PUBLIC_KEY"),
        )

        RealIssuersRepository(
            configuredIssuers = listOf(theOnlyConfiguredIssuer),
        )
    } bind IssuersRepository::class

    single {
        val timeout = getPropertyOrNull<String>("KEY_RENEWAL_TIMEOUT")
            ?.let(Duration::parse)

        if (timeout != null) {
            InMemoryTimeoutKeyRenewalAllowanceRepository(
                renewalTimeout = timeout,
            )
        } else {
            AlwaysAllowingKeyRenewalAllowanceRepository
        }
    } bind KeyRenewalAllowanceRepository::class
}
