package com.quotation.domain.usecase

import com.quotation.domain.model.CoinModel
import io.reactivex.Flowable

interface BaseUseCase {
    operator fun invoke(InstrumentId: Int): Flowable<CoinModel>
}