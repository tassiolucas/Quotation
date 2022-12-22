package com.quotation.presentation.ui

import androidx.lifecycle.LifecycleOwner
import com.quotation.data.InstrumentId
import com.quotation.domain.usecase.ObserveGetInstrumentUseCase
import com.quotation.domain.usecase.ObserverSubscribeLevel1UseCase
import com.quotation.dummyInstrumentModel
import com.quotation.dummySubscribeModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuotationViewModelTest {

    private val observerGetInstrumentUseCase = mockk<ObserveGetInstrumentUseCase>()
    private val observerSubscribeLevel1UseCase = mockk<ObserverSubscribeLevel1UseCase>()
    private val lifecycleOwner = mockk<LifecycleOwner>()

    private lateinit var quotationViewModel: QuotationViewModel

    @Before
    fun setUp() {

    }

    @Before
    fun before() {
        quotationViewModel = QuotationViewModel(
            observerGetInstrumentUseCase,
            observerSubscribeLevel1UseCase
        )

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `startCoinsList - Start loading coins names and InstrumentId`() {
        every {
            observerGetInstrumentUseCase.invoke()
        } returns Flowable.just(listOf(dummyInstrumentModel))

        val dummyCoinBindableItem = CoinBindableItem(
            lifecycleOwner,
            CoinBindableItem.Config(
                imageRes = InstrumentId.BTC.imgRes,
                index = InstrumentId.getPosition(1),
                nameTitle = InstrumentId.BTC.coinName,
                symbolTitle = InstrumentId.BTC.name
            )
        )

        quotationViewModel.startCoinsList(lifecycleOwner, {})

        verify(exactly = 1) {
            observerGetInstrumentUseCase.invoke()
        }

        assertEquals(
            quotationViewModel.coinBindableItems[0].index,
            listOf(dummyCoinBindableItem)[0].index
        )
        assertEquals(
            quotationViewModel.coinBindableItems[0].currencyTitleValue.value,
            listOf(dummyCoinBindableItem)[0].currencyTitleValue.value
        )
        assertEquals(
            quotationViewModel.coinBindableItems[0].variationTitleValue.value,
            listOf(dummyCoinBindableItem)[0].variationTitleValue.value
        )
    }

    @Test
    fun `loadCoinsList - when start screen, subscribe on coins availables`() {
        every {
            observerGetInstrumentUseCase.invoke()
        } returns Flowable.just(listOf(dummyInstrumentModel))
        every {
            observerSubscribeLevel1UseCase.invoke(1)
        } returns Flowable.just(dummySubscribeModel)

        val dummyCoinBindableItem = CoinBindableItem(
            lifecycleOwner,
            CoinBindableItem.Config(
                imageRes = InstrumentId.BTC.imgRes,
                index = InstrumentId.getPosition(1),
                nameTitle = InstrumentId.BTC.coinName,
                symbolTitle = InstrumentId.BTC.name
            )
        )

        quotationViewModel.startCoinsList(lifecycleOwner, {})

        verify(exactly = 1) {
            observerGetInstrumentUseCase.invoke()
        }

        quotationViewModel.loadCoinsList(listOf(dummyInstrumentModel)) {}

        verify(exactly = 1) {
            observerSubscribeLevel1UseCase.invoke(1)
        }

        assertEquals(
            quotationViewModel.coinBindableItems[0].index,
            listOf(dummyCoinBindableItem)[0].index
        )
        assertEquals(
            quotationViewModel.coinBindableItems[0].currencyTitleValue.value,
            listOf(dummyCoinBindableItem)[0].currencyTitleValue.value
        )
        assertEquals(
            quotationViewModel.coinBindableItems[0].variationTitleValue.value,
            listOf(dummyCoinBindableItem)[0].variationTitleValue.value
        )
    }
}