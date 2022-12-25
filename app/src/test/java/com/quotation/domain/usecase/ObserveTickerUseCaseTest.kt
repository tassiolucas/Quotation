package com.quotation.domain.usecase

import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class ObserveTickerUseCaseTest {

    private var coinbaseRepository: CoinbaseRepository = mockk()
    private lateinit var observeTickerUseCase: ObserveTickerUseCase

    @Before
    fun setup() {
        observeTickerUseCase = ObserveTickerUseCase(
            coinbaseRepository = coinbaseRepository
        )
    }

    @Test
    fun `execute - runs with success, them react to ticker list`() {
        every {
            coinbaseRepository.observeTicker()
        } returns Flowable.just(mockk())

        observeTickerUseCase.execute(UseCase.None())

        verify(exactly = 1) {
            coinbaseRepository.observeTicker()
        }
    }

    @Test
    fun `execute - runs with error, them react with throwable`() {
        every {
            coinbaseRepository.observeTicker()
        } returns Throwable().let { Flowable.error(it) }

        observeTickerUseCase.execute(UseCase.None()).test().assertError(Throwable::class.java)

        verify(exactly = 1) {
            coinbaseRepository.observeTicker()
        }
    }
}