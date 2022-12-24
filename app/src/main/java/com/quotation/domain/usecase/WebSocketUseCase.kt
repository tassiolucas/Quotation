package com.quotation.domain.usecase

import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase.FlowableUseCase
import com.quotation.domain.usecase.base.UseCase.None
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable

class WebSocketUseCase(
    private val coinbaseRepository: CoinbaseRepository
) : FlowableUseCase<None, WebSocket.Event> {

    override fun execute(params: None): Flowable<WebSocket.Event> =
        coinbaseRepository.observeWebSocketEvent()
}