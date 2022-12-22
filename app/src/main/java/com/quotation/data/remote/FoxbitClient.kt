package com.quotation.data.remote

import com.quotation.data.entity.BaseCoinResponse
import com.quotation.data.entity.CoinRequest
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface FoxbitClient {

    @Send
    fun getInstrumentId(getInstrumentIdRequest: CoinRequest): Flowable<BaseCoinResponse>

    @Send
    fun subscribeLevel1(subscribeLevel1Request: CoinRequest): Flowable<BaseCoinResponse>

}