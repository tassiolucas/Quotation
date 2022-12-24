package com.quotation.domain.usecase

import com.quotation.data.entity.Subscribe
import com.quotation.data.repository.CoinbaseRepository
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.usecase.SendSubscribeUseCase.SubscribeParams
import com.quotation.domain.usecase.base.UseCase.Params
import com.quotation.domain.usecase.base.UseCase.RequestUseCase

class SendSubscribeUseCase(
    private val coinbaseRepository: CoinbaseRepository
) : RequestUseCase<SubscribeParams, Unit> {

    override fun execute(params: SubscribeParams) =
        coinbaseRepository.sendSubscribe(Subscribe(params.m, params.i, params.n, params.o))

    class SubscribeParams(
        val m: Int = BaseSubscribe.M,
        val i: Int = BaseSubscribe.I,
        val n: String = "",
        val o: String = ""
    ) : Params()
}