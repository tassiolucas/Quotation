package com.quotation.presentation.ui

import com.quotation.*
import com.quotation.data.InstrumentId
import com.quotation.domain.usecase.ObserveTickerListUseCase
import com.quotation.domain.usecase.ObserveTickerUseCase
import com.quotation.domain.usecase.SendSubscribeUseCase
import com.quotation.domain.usecase.WebSocketUseCase
import com.quotation.ext.Executors
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QuotationViewModelTest {

    private val observeTickerListUseCase = mockk<ObserveTickerListUseCase>()
    private val observeTickerUseCase = mockk<ObserveTickerUseCase>()
    private val sendSubscribeUseCase = mockk<SendSubscribeUseCase>()
    private val webSocketUseCase = mockk<WebSocketUseCase>()
    private val executors = mockk<Executors>()

    private lateinit var quotationViewModel: QuotationViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        quotationViewModel = QuotationViewModel(
            webSocketUseCase,
            observeTickerUseCase,
            observeTickerListUseCase,
            sendSubscribeUseCase,
            executors
        )
    }

    @Test
    fun `startCoinsList - when start app, them observer the webSocket connection and tickersList`() =
        runBlocking {
            every {
                webSocketUseCase.execute(any())
            } returns Flowable.just(mockk())

            every {
                sendSubscribeUseCase.execute(dummyGetInstrumentsParams)
            } returns Unit

            every {
                observeTickerListUseCase.execute(any())
            } returns Flowable.just(dummyTickerList)

            every {
                executors.io
            } returns Schedulers.io()

            every {
                executors.ui
            } returns Schedulers.io()

            quotationViewModel.coinList.observeForever {
                assertEquals(dummyCoin.instrumentId, it[0].instrumentId)
                assertEquals(dummyCoin.sortIndex, it[0].sortIndex)
                assertEquals(dummyCoin.symbol, it[0].symbol)
                assertEquals(dummyCoin.lastTradedPx, it[0].lastTradedPx)
                assertEquals(dummyCoin.rolling24HrVolume, it[0].rolling24HrVolume)
                assertEquals(dummyCoin.rolling24HrPxChange, it[0].rolling24HrPxChange)
            }

            quotationViewModel.updateEvent.observeForever {
                assertEquals(Unit, it)
            }

            quotationViewModel.startCoinsList()

            verify(exactly = 2) {
                webSocketUseCase.execute(any())
            }

            verify(exactly = 1) {
                sendSubscribeUseCase.execute(any())
            }

            assertEquals(
                quotationViewModel.coinBindableItems[0].index,
                dummyCoinBindableItem.index
            )
            assertEquals(quotationViewModel.coinBindableItems[0].currencyTitleValue.value, null)
            assertEquals(quotationViewModel.coinBindableItems[0].variationTitleValue.value, null)

            return@runBlocking
        }

    @Test
    fun `loadCoinsList - when load coins values list, them update values in cards`() = runBlocking {
        every {
            webSocketUseCase.execute(any())
        } returns Flowable.just(mockk())

        every {
            sendSubscribeUseCase.execute(any())
        } returns Unit

        every {
            observeTickerListUseCase.execute(any())
        } returns Flowable.just(dummyTickerList)

        every {
            observeTickerUseCase.execute(any())
        } returns Flowable.just(dummyTicker)

        every {
            executors.io
        } returns Schedulers.io()

        every {
            executors.ui
        } returns Schedulers.io()

        quotationViewModel.startCoinsList()

        quotationViewModel.coinBindableItems[0].currencyTitleValue.observeForever {
            assertEquals(dummyCoinBindableItem.currencyTitleValue, it)
        }

        quotationViewModel.coinBindableItems[0].variationTitleValue.observeForever {
            assertEquals(dummyCoinBindableItem.variationTitleValue, it)
        }

        quotationViewModel.updateEvent.observeForever {
            assertEquals(Unit, it)
        }

        quotationViewModel.loadCoinsList(listOf(dummyCoin))

        coVerify(exactly = 2) {
            sendSubscribeUseCase.execute(any())
        }

        assertEquals(
            quotationViewModel.coinBindableItems[0].index,
            dummyCoinBindableItem.index
        )
    }
}