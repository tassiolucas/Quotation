package com.quotation.data.remote

import android.annotation.SuppressLint
import com.quotation.data.entity.BaseCoinResponse
import com.quotation.data.entity.CoinRequest
import com.quotation.domain.entities.BaseSubscribe.Companion.GET_INSTRUMENTS
import com.quotation.domain.entities.BaseSubscribe.Companion.SUBSCRIBE_LEVEL_1
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@SuppressLint("CheckResult")
class FoxbitClientImpl(private val foxbitApi: FoxbitApi) : FoxbitClient {

    override fun getInstrumentId(getInstrumentIdRequest: CoinRequest): Flowable<BaseCoinResponse> {
        foxbitApi.openWebSocketEventList()
            .filter {
                it is WebSocket.Event.OnConnectionOpened<*>
            }
            .subscribe({
                foxbitApi.sendGetInstrumentId(getInstrumentIdRequest)
            }, { e ->
                Timber.e(e)
            })

        return foxbitApi.observerCoinList()
            .subscribeOn(Schedulers.io())
            .filter {
                it.n == GET_INSTRUMENTS
            }
    }

    override fun subscribeLevel1(subscribeLevel1Request: CoinRequest): Flowable<BaseCoinResponse> {
        foxbitApi.openWebSocketEvent()
            .filter {
                it is WebSocket.Event.OnConnectionOpened<*>
            }
            .subscribe({
                foxbitApi.subscribeLevel1(subscribeLevel1Request)
            }, { e ->
                Timber.e(e)
            })

        return foxbitApi.observerCoin()
            .subscribeOn(Schedulers.io())
            .filter {
                it.n == SUBSCRIBE_LEVEL_1
            }
    }

}