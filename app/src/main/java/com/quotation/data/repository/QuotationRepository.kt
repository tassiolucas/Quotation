package com.quotation.data.repository

import com.quotation.domain.entities.Coin
import com.quotation.domain.model.CoinModel
import io.reactivex.Flowable

interface QuotationRepository {

    fun getCoins(coin: Coin): Flowable<List<CoinModel>>

    fun subscribeCoins(coin: Coin): Flowable<CoinModel>
}