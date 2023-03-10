package com.quotation.data.remote

import com.quotation.data.entity.Subscribe
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface FoxbitApi {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeTicker(): Flowable<Ticker>

    @Receive
    fun observeTickerList(): Flowable<TickerList>

    @Send
    fun sendSubscribe(subscribe: Subscribe)

    companion object {
        const val BASE_URI = "wss://api.foxbit.com.br?origin=android"
    }
}