package com.quotation.domain.usecase

import com.google.gson.Gson
import com.quotation.data.entity.CoinDataRequest
import com.quotation.data.repository.QuotationRepository
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.entities.Coin
import com.quotation.domain.model.CoinModel
import io.reactivex.Flowable

class ObserverSubscribeLevel1UseCase(private val quotationRepository: QuotationRepository) {

    operator fun invoke(InstrumentId: Int): Flowable<CoinModel> {
        val data = Gson().toJson(CoinDataRequest(InstrumentId = InstrumentId))

        val subscribeCoin = Coin(
            m = BaseSubscribe.M,
            i = BaseSubscribe.I,
            n = BaseSubscribe.SUBSCRIBE_LEVEL_1,
            o = data
        )

        return quotationRepository.subscribeCoins(subscribeCoin)
    }
}