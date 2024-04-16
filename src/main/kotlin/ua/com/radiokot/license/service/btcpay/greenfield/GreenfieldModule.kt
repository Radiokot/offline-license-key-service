package ua.com.radiokot.license.service.btcpay.greenfield

import okhttp3.OkHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.GreenfieldInvoicesApi
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.ioModule
import ua.com.radiokot.license.service.util.HeaderInterceptor

val greenfieldModule = module {
    includes(ioModule)

    single {
        get<Retrofit.Builder>()
            .baseUrl(getNotEmptyProperty("GREENFIELD_BASE_URL"))
            .client(
                get<OkHttpClient.Builder>()
                    .addInterceptor(
                        HeaderInterceptor.authorization(
                            authorization = getNotEmptyProperty("GREENFIELD_AUTHORIZATION"),
                        )
                    )
                    .build()
            )
            .build()
            .create(GreenfieldInvoicesApi::class.java)
    } bind GreenfieldInvoicesApi::class
}
