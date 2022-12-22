package com.quotation.di

import android.app.Application
import com.google.gson.Gson
import com.quotation.data.remote.FoxbitApi
import com.quotation.data.remote.FoxbitClient
import com.quotation.data.remote.FoxbitClientImpl
import com.quotation.data.repository.QuotationRepositoryImpl
import com.quotation.data.repository.QuotationRepository
import com.quotation.domain.usecase.ObserveGetInstrumentUseCase
import com.quotation.domain.usecase.ObserverSubscribeLevel1UseCase
import com.quotation.presentation.ui.QuotationViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { createAndroidLifecycle(application = get()) }

    single { createOkHttpClient() }

    single { createScarlet(okHttpClient = get(), lifecycle = get()) }

    single<FoxbitClient> { FoxbitClientImpl(foxbitApi = get()) }

    single<QuotationRepository> {
        QuotationRepositoryImpl(
            foxbitClient = get()
        )
    }

    factory {
        ObserveGetInstrumentUseCase(quotationRepository = get())
    }

    factory {
        ObserverSubscribeLevel1UseCase(quotationRepository = get())
    }

    viewModel {
        QuotationViewModel(
            observerCoinUseCase = get(),
            observerSubscribeLevel1UseCase = get()
        )
    }
}

private fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}

private fun createAndroidLifecycle(application: Application): Lifecycle {
    return AndroidLifecycle.ofApplicationForeground(application)
}

val jsonMoshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val gson = Gson()

private fun createScarlet(okHttpClient: OkHttpClient, lifecycle: Lifecycle): FoxbitApi {

    return Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory(FoxbitApi.BASE_URI))
        .lifecycle(lifecycle)
        .addMessageAdapterFactory(MoshiMessageAdapter.Factory(jsonMoshi))
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()
        .create()
}