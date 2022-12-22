package com.quotation.data.repository

import com.google.gson.Gson
import com.quotation.*
import com.quotation.data.entity.GetInstrumentIdResponse
import com.quotation.data.entity.SubscribeLevel1Response
import com.quotation.data.entity.toCoinRequest
import com.quotation.data.remote.FoxbitClient
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class QuotationRepositoryImplTest {

    private val foxbitClient = mockk<FoxbitClient>()
    private val gson = mockk<Gson>()
    private lateinit var quotationRepositoryImpl: QuotationRepositoryImpl

    @Before
    fun before() {
        quotationRepositoryImpl = QuotationRepositoryImpl(
            foxbitClient = foxbitClient, gson = gson
        )
    }

    @Test
    fun `startCoinsList - Start loading coins names and InstrumentId`() {
        every {
            foxbitClient.getInstrumentId(dummyCoin.toCoinRequest())
        } returns Flowable.just(dummyInstrumentBaseCoinResponse)

        every {
            gson.fromJson<List<GetInstrumentIdResponse>>(
                dummyInstrumentJson, dummyType
            )
        } returns listOf(
            dummyInstrumentResponse
        )

        quotationRepositoryImpl.getCoins(dummyCoin).test().assertValue { coins ->
            coins[0].InstrumentId == dummyInstrumentModel.InstrumentId &&
                    coins[0].Symbol == dummyInstrumentModel.Symbol &&
                    coins[0].SortIndex == dummyInstrumentModel.SortIndex
        }
    }

    @Test
    fun `subscribeCoins - when start screen, subscribe on coins availables`() {
        every {
            foxbitClient.subscribeLevel1(dummyCoin.toCoinRequest())
        } returns Flowable.just(dummyInstrumentBaseCoinResponse)

        every {
            gson.fromJson(
                dummyInstrumentJson, SubscribeLevel1Response::class.java
            )
        } returns dummySubscribeResponse

        quotationRepositoryImpl.subscribeCoins(dummyCoin).test().assertValue { coin ->
            coin.InstrumentId == dummySubscribeModel.InstrumentId &&
                    coin.Symbol == dummySubscribeModel.Symbol &&
                    coin.SortIndex == dummySubscribeModel.SortIndex &&
                    coin.LastTradedPx == dummySubscribeModel.LastTradedPx &&
                    coin.Rolling24HrVolume == dummySubscribeModel.Rolling24HrVolume &&
                    coin.Rolling24HrPxChange == dummySubscribeModel.Rolling24HrPxChange
        }
    }

}