package com.quotation.domain.usecase

import com.quotation.data.entity.Ticker
import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase.FlowableUseCase
import com.quotation.domain.usecase.base.UseCase.None
import io.reactivex.Flowable

class ObserveTickerUseCase(
    private val coinbaseRepository: CoinbaseRepository
) : FlowableUseCase<None, Ticker> {

    override fun execute(params: None): Flowable<Ticker> =
        coinbaseRepository.observeTicker()
}