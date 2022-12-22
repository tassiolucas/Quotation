package com.quotation.domain.usecase

import com.quotation.data.repository.QuotationRepository
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.entities.Coin
import com.quotation.domain.model.CoinModel
import io.reactivex.Flowable

class ObserveGetInstrumentUseCase(private val quotationRepository: QuotationRepository) {

    operator fun invoke(): Flowable<List<CoinModel>> {
        val getCoin = Coin(
            m = BaseSubscribe.M,
            i = BaseSubscribe.I,
            n = BaseSubscribe.GET_INSTRUMENTS,
            o = "{}"
        )

        return quotationRepository.getCoins(getCoin)
    }
}