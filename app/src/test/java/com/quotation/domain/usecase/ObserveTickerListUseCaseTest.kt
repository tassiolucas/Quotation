package com.quotation.domain.usecase

import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase.None
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class ObserveTickerListUseCaseTest {

    private val coinbaseRepository: CoinbaseRepository = mockk()
    private lateinit var observeTickerListUseCase: ObserveTickerListUseCase

    @Before
    fun setup() {
        observeTickerListUseCase = ObserveTickerListUseCase(
            coinbaseRepository = coinbaseRepository
        )
    }

    @Test
    fun `execute - runs with success, them react to ticker list`() {
        every {
            coinbaseRepository.observeTickerList()
        } returns Flowable.just(mockk())

        observeTickerListUseCase.execute(None())

        verify(exactly = 1) {
            coinbaseRepository.observeTickerList()
        }
    }

    @Test
    fun `execute - runs with error, them react with throwable`() {
        every {
            coinbaseRepository.observeTickerList()
        } returns Throwable().let { Flowable.error(it) }

        observeTickerListUseCase.execute(None()).test().assertError(Throwable::class.java)

        verify(exactly = 1) {
            coinbaseRepository.observeTickerList()
        }
    }
}