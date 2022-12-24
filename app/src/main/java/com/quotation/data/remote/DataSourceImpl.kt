package com.quotation.data.remote

import com.quotation.data.entity.Subscribe
import com.quotation.data.entity.TickerList
import io.reactivex.Flowable

class DataSourceImpl(private val foxbitApi: FoxbitApi) : DataSource {

    override fun observeWebSocketEvent() =
        foxbitApi.observeWebSocketEvent()

    override fun observeTicker() = foxbitApi.observeTicker()

    override fun observeTickerList(): Flowable<TickerList> = foxbitApi.observeTickerList()

    override fun sendSubscribe(subscribe: Subscribe) = foxbitApi.sendSubscribe(subscribe)
}