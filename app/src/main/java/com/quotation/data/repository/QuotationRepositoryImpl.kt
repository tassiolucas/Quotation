package com.quotation.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quotation.data.entity.GetInstrumentIdResponse
import com.quotation.data.entity.SubscribeLevel1Response
import com.quotation.data.entity.toCoinRequest
import com.quotation.data.remote.FoxbitClient
import com.quotation.domain.entities.Coin
import com.quotation.domain.model.CoinModel
import com.quotation.domain.model.toCoinModel
import com.quotation.domain.model.toSubscribeLevel1Model
import io.reactivex.Flowable

class QuotationRepositoryImpl(
    private val foxbitClient: FoxbitClient,
    private val gson: Gson
) : QuotationRepository {

    override fun getCoins(coin: Coin): Flowable<List<CoinModel>> =
        foxbitClient.getInstrumentId(coin.toCoinRequest())
            .map { response ->
                val type = object : TypeToken<List<GetInstrumentIdResponse>>() {}.type

                gson.fromJson<List<GetInstrumentIdResponse>>(response.o, type)?.map {
                    it.toCoinModel()
                }?.filter {
                    it.InstrumentId == 1 ||
                            it.InstrumentId == 10 ||
                            it.InstrumentId == 6 ||
                            it.InstrumentId == 4 ||
                            it.InstrumentId == 2
                }
            }

    override fun subscribeCoins(coin: Coin): Flowable<CoinModel> =
        foxbitClient.subscribeLevel1(coin.toCoinRequest())
            .map { response ->
                gson.fromJson(response.o, SubscribeLevel1Response::class.java)
                    .toSubscribeLevel1Model()
            }
}