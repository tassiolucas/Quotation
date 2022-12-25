package com.quotation.domain.usecase

import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase.None
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class WebSocketUseCaseTest {

    private var coinbaseRepository: CoinbaseRepository = mockk()
    private lateinit var webSocketUseCase: WebSocketUseCase

    @Before
    fun setup() {
        webSocketUseCase = WebSocketUseCase(
            coinbaseRepository = coinbaseRepository
        )
    }

    @Test
    fun `execute - runs with success, them react to ticker list`() {
        every {
            coinbaseRepository.observeWebSocketEvent()
        } returns Flowable.just(mockk())

        webSocketUseCase.execute(None())

        verify(exactly = 1) {
            coinbaseRepository.observeWebSocketEvent()
        }
    }

    @Test
    fun `execute - runs with error, them react with throwable`() {
        every {
            coinbaseRepository.observeWebSocketEvent()
        } returns Throwable().let { Flowable.error(it) }

        webSocketUseCase.execute(None()).test().assertError(Throwable::class.java)

        verify(exactly = 1) {
            coinbaseRepository.observeWebSocketEvent()
        }
    }
}