package com.quotation.domain.usecase

import com.quotation.data.repository.CoinbaseRepository
import com.quotation.dummySubscribeLevel1Params
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SendSubscribeUseCaseTest {

    private var coinbaseRepository: CoinbaseRepository = mockk()
    private lateinit var subscribeUseCase: SendSubscribeUseCase

    @Before
    fun setup() {
        subscribeUseCase = SendSubscribeUseCase(
            coinbaseRepository = coinbaseRepository
        )
    }

    @Test
    fun `execute - runs with success, them react to ticker list or no`() {
        every {
            coinbaseRepository.sendSubscribe(any())
        } returns Unit

        subscribeUseCase.execute(dummySubscribeLevel1Params)

        verify(exactly = 1) {
            coinbaseRepository.sendSubscribe(any())
        }
    }
}