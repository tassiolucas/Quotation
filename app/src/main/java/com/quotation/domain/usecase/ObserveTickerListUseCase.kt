package com.quotation.domain.usecase

import com.quotation.data.entity.TickerList
import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.usecase.base.UseCase.FlowableUseCase
import com.quotation.domain.usecase.base.UseCase.None
import io.reactivex.Flowable

class ObserveTickerListUseCase(
    private val coinbaseRepository: CoinbaseRepository
) : FlowableUseCase<None, TickerList> {

    override fun execute(params: None): Flowable<TickerList> =
        coinbaseRepository.observeTickerList()
}