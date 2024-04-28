package ua.com.radiokot.license.service.turnstile

import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.ioModule

val cloudflareTurnstileModule = module {
    if (System.getenv("CLOUDFLARE_TURNSTILE") != null) {
        includes(ioModule)
        single {
          RealCloudflareTurnstile(
              siteKey = getNotEmptyProperty("CLOUDFLARE_TURNSTILE_SITE_KEY"),
              secretKey = getNotEmptyProperty("CLOUDFLARE_TURNSTILE_SECRET_KEY"),
              httpClient = get(),
              jsonObjectMapper = get(),
          )
        } bind CloudflareTurnstile::class
    }
}
