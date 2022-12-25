package com.quotation.data.remote

import com.quotation.data.entity.Coin
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.quotation.domain.entities.BaseSubscribe
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

class DataSourceImplTest {

    private val foxbitApi: FoxbitApi = mockk()
    private lateinit var dataSourceImpl: DataSourceImpl

    @Before
    fun setup() {
        dataSourceImpl = DataSourceImpl(
            foxbitApi = foxbitApi
        )
    }

    @Test
    fun observeWebSocketEvent() {
        every {
            foxbitApi.observeWebSocketEvent()
        } returns Flowable.just(mockk())

        dataSourceImpl.observeWebSocketEvent().test().assertValue { event ->
            event is WebSocket.Event.OnConnectionOpened<*>
        }
    }

    @Test
    fun observeTicker() {
        every {
            foxbitApi.observeTicker()
        } returns Flowable.just(dummyTicker)

        dataSourceImpl.observeTicker().test().assertValue(
            Ticker(
                m = BaseSubscribe.M,
                i = BaseSubscribe.I,
                n = BaseSubscribe.GET_INSTRUMENTS,
                o = Coin()
            )
        )
    }

    @Test
    fun observeTickerList() {
        every {
            foxbitApi.observeTickerList()
        } returns Flowable.just(dummyTickerList)

        dataSourceImpl.observeTickerList().test().assertValue(
            TickerList(
                m = BaseSubscribe.M,
                i = BaseSubscribe.I,
                n = BaseSubscribe.GET_INSTRUMENTS,
                o = listOf(
                    Coin(
                        instrumentId = 1,
                        symbol = "BTC",
                        sortIndex = 1,
                        lastTradedPx = 1.0,
                        rolling24HrVolume = 1.0,
                        rolling24HrPxChange = 1.0,
                    )
                )
            )
        )
    }

    @Test
    fun sendSubscribe() {
        every {
            foxbitApi.sendSubscribe(dummySubscribe)
        } returns Unit

        dataSourceImpl.sendSubscribe(dummySubscribe)

        verify(exactly = 1) {
            foxbitApi.sendSubscribe(dummySubscribe)
        }
    }
}