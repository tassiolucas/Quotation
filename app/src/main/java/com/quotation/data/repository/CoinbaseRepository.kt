package com.quotation.data.repository

import com.quotation.data.entity.Subscribe
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable

interface CoinbaseRepository {

    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    fun observeTicker(): Flowable<Ticker>

    fun observeTickerList(): Flowable<TickerList>

    fun sendSubscribe(subscribe: Subscribe)

}