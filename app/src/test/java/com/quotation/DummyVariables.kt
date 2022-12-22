package com.quotation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quotation.data.entity.BaseCoinResponse
import com.quotation.data.entity.GetInstrumentIdResponse
import com.quotation.data.entity.SubscribeLevel1Response
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.entities.Coin
import com.quotation.domain.model.CoinModel
import com.quotation.presentation.ui.CoinBindableItem
import java.lang.reflect.Type

val dummyInstrumentResponse = GetInstrumentIdResponse(1, "BTC", 0)

val dummySubscribeResponse = SubscribeLevel1Response(1, 0.0, 0.0, 0.0)

val dummyInstrumentJson: String = Gson().toJson(dummyInstrumentResponse)

val dummySubscribeJson: String = Gson().toJson(dummySubscribeResponse)

val dummyType: Type = object : TypeToken<List<GetInstrumentIdResponse>>() {}.type

val dummyCoin = Coin(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = "{}"
)

val dummyInstrumentBaseCoinResponse = BaseCoinResponse(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = dummyInstrumentJson
)

val dummyInstrumentModel = CoinModel(
    InstrumentId = 1,
    Symbol = "BTC",
    SortIndex = 0
)

val dummySubscribeModel = CoinModel(
    InstrumentId = 1,
    Symbol = "",
    SortIndex = null,
    LastTradedPx = 0.0,
    Rolling24HrVolume = 0.0,
    Rolling24HrPxChange = 0.0
)

