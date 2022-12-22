package com.quotation.data.remote

import com.quotation.data.entity.BaseCoinResponse
import com.quotation.data.entity.CoinRequest
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface FoxbitApi {

    @Receive
    fun openWebSocketEventList(): Flowable<WebSocket.Event>

    @Receive
    fun openWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observerCoinList(): Flowable<BaseCoinResponse>

    @Receive
    fun observerCoin(): Flowable<BaseCoinResponse>

    @Send
    fun sendGetInstrumentId(subscribeRequest: CoinRequest)

    @Send
    fun subscribeLevel1(subscribeLevel1Request: CoinRequest)

    companion object {
        const val BASE_URI = "wss://api.foxbit.com.br?origin=android"
    }
}