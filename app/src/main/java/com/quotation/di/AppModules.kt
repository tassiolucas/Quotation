package com.quotation.di

import android.app.Application
import com.quotation.data.TickerJsonAdapter
import com.quotation.data.TickerListJsonAdapter
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.quotation.data.remote.DataSource
import com.quotation.data.remote.DataSourceImpl
import com.quotation.data.remote.FoxbitApi
import com.quotation.data.repository.CoinbaseRepository
import com.quotation.data.repository.CoinbaseRepositoryImpl
import com.quotation.domain.usecase.ObserveTickerListUseCase
import com.quotation.domain.usecase.ObserveTickerUseCase
import com.quotation.domain.usecase.SendSubscribeUseCase
import com.quotation.domain.usecase.WebSocketUseCase
import com.quotation.ext.ComputationThread
import com.quotation.ext.ExecutionThread
import com.quotation.ext.Executors
import com.quotation.ext.PostExecutionThread
import com.quotation.presentation.ui.QuotationViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single { createAndroidLifecycle(application = get()) }

    single { createOkHttpClient() }

    single { createScarlet(okHttpClient = get(), lifecycle = get()) }

    single { }

    single {
        Executors(
            executionThread = get(),
            postExecutionThread = get(),
            computationThread = get()
        )
    }

    single<ExecutionThread> {
        object : ExecutionThread {
            override val scheduler: Scheduler
                get() = Schedulers.io()
        }
    }

    single<PostExecutionThread> {
        object : PostExecutionThread {
            override val scheduler: Scheduler
                get() = Schedulers.io()
        }
    }

    single<ComputationThread> {
        object : ComputationThread {
            override val scheduler: Scheduler
                get() = Schedulers.io()
        }
    }

    single<DataSource> { DataSourceImpl(foxbitApi = get()) }

    single<CoinbaseRepository> {
        CoinbaseRepositoryImpl(
            dataSource = get()
        )
    }

    factory {
        WebSocketUseCase(coinbaseRepository = get())
    }

    factory {
        ObserveTickerUseCase(coinbaseRepository = get())
    }

    factory {
        ObserveTickerListUseCase(coinbaseRepository = get())
    }

    factory {
        SendSubscribeUseCase(coinbaseRepository = get())
    }

    viewModel {
        QuotationViewModel(
            webSocketUseCase = get(),
            observeTickerUseCase = get(),
            observeTickerListUseCase = get(),
            sendSubscribeUseCase = get(),
            executors = get()
        )
    }
}

private const val DEFAULT_TIMEOUT_IN_SEC = 10L
private const val DEFAULT_BASE_DURATION_IN_MS = 5000L
private const val DEFAULT_MAX_DURATION_IN_MS = 5000L

private fun createOkHttpClient() = OkHttpClient.Builder()
    .connectTimeout(DEFAULT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
    .readTimeout(DEFAULT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
    .writeTimeout(DEFAULT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
    .build()

private fun createAndroidLifecycle(application: Application) =
    AndroidLifecycle.ofApplicationForeground(application)

private fun createMoshi(): Moshi {
    val moshi = Moshi.Builder()
        .build()

    return Moshi.Builder()
        .add(Ticker::class.java, TickerJsonAdapter())
        .add(TickerList::class.java, TickerListJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()
}

private fun createScarlet(okHttpClient: OkHttpClient, lifecycle: Lifecycle): FoxbitApi =
    Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory(FoxbitApi.BASE_URI))
        .lifecycle(lifecycle)
        .backoffStrategy(createBackoffStrategy())
        .addMessageAdapterFactory(MoshiMessageAdapter.Factory(createMoshi()))
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()
        .create()

private fun createBackoffStrategy() = ExponentialWithJitterBackoffStrategy(
    DEFAULT_BASE_DURATION_IN_MS,
    DEFAULT_MAX_DURATION_IN_MS
)