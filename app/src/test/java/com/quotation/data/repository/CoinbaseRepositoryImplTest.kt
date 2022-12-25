package com.quotation.data.repository

import com.quotation.data.entity.Coin
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.quotation.data.remote.DataSource
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.dummyCoin
import com.quotation.dummySubscribe
import com.quotation.dummyTicker
import com.quotation.dummyTickerList
import com.tinder.scarlet.WebSocket
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class CoinbaseRepositoryImplTest {

    private val dataSource = mockk<DataSource>()
    private lateinit var quotationRepositoryImpl: CoinbaseRepositoryImpl

    @Before
    fun before() {
        quotationRepositoryImpl = CoinbaseRepositoryImpl(
            dataSource = dataSource
        )
    }

    @Test
    fun `observeWebSocketEvent - on open web socket connection, them receive OnConnected Event`() {
        every {
            dataSource.observeWebSocketEvent()
        } returns Flowable.just(mockk())

        quotationRepositoryImpl.observeWebSocketEvent().test().assertValue { event ->
            event is WebSocket.Event.OnConnectionOpened<*>
        }
    }

    @Test
    fun `observeTicker - on observeTicker, react to ticker stream`() {
        every {
            dataSource.observeTicker()
        } returns Flowable.just(dummyTicker)

        quotationRepositoryImpl.observeTicker().test().assertValue(
            Ticker(
                m = BaseSubscribe.M,
                i = BaseSubscribe.I,
                n = BaseSubscribe.GET_INSTRUMENTS,
                o = Coin()
            )
        )
    }

    @Test
    fun `observeTickerList - on observeTickerList, react to tickerList stream`() {
        every {
            dataSource.observeTickerList()
        } returns Flowable.just(dummyTickerList)

        quotationRepositoryImpl.observeTickerList().test().assertValue(
            TickerList(
                m = BaseSubscribe.M,
                i = BaseSubscribe.I,
                n = BaseSubscribe.GET_INSTRUMENTS,
                o = listOf(dummyCoin)
            )
        )
    }

    @Test
    fun `sendSubscribe - when sendSubscribe, emmit a just one Subscribe`() {
        every {
            dataSource.sendSubscribe(dummySubscribe)
        } returns Unit

        quotationRepositoryImpl.sendSubscribe(dummySubscribe)

        verify(exactly = 1) {
            quotationRepositoryImpl.sendSubscribe(dummySubscribe)
        }
    }
}