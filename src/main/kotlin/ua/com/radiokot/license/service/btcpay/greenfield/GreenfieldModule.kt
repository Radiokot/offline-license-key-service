package ua.com.radiokot.license.service.btcpay.greenfield

import okhttp3.OkHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.GreenfieldInvoicesApi
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.GreenfieldStoreInvoicesService
import ua.com.radiokot.license.service.btcpay.greenfield.invocies.RealGreenfieldStoreInvoicesService
import ua.com.radiokot.license.service.extension.getNotEmptyProperty
import ua.com.radiokot.license.service.ioModule
import ua.com.radiokot.license.service.jsonapi.di.jsonApiModule

val greenfieldModule = module {
    includes(ioModule)

    single {
        Retrofit.Builder()
            .baseUrl(getNotEmptyProperty("GREENFIELD_BASE_URL"))
            .client(get())
            .addConverterFactory(JacksonConverterFactory.create(get()))
            .build()
            .create(GreenfieldInvoicesApi::class.java)
    } bind GreenfieldInvoicesApi::class

    single {
        RealGreenfieldStoreInvoicesService(
            storeId = getNotEmptyProperty("GREENFIELD_STORE_ID"),
            authorization = getNotEmptyProperty("GREENFIELD_AUTHORIZATION"),
            greenfieldInvoicesApi = get(),
        )
    } bind GreenfieldStoreInvoicesService::class
}
