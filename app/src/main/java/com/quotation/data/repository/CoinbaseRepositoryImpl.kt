package com.quotation.data.repository

import com.quotation.data.entity.Subscribe
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.quotation.data.remote.DataSource
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable

class CoinbaseRepositoryImpl(
    private val dataSource: DataSource
) : CoinbaseRepository {

    override fun observeWebSocketEvent(): Flowable<WebSocket.Event> =
        dataSource.observeWebSocketEvent()

    override fun observeTicker(): Flowable<Ticker> = dataSource.observeTicker()

    override fun observeTickerList(): Flowable<TickerList> = dataSource.observeTickerList()

    override fun sendSubscribe(subscribe: Subscribe) = dataSource.sendSubscribe(subscribe)
}