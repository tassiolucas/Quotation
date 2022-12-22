package com.quotation.domain.model

import com.quotation.data.entity.GetInstrumentIdResponse
import com.quotation.data.entity.SubscribeLevel1Response

data class CoinModel(
    val InstrumentId: Int,
    val Symbol: String = "",
    val SortIndex: Int? = null,
    val LastTradedPx: Number? = null,
    val Rolling24HrVolume: Number? = null,
    val Rolling24HrPxChange: Number? = null
)

fun GetInstrumentIdResponse.toCoinModel() = CoinModel(
    InstrumentId = this.InstrumentId,
    Symbol = this.Symbol,
    SortIndex = this.SortIndex
)

fun SubscribeLevel1Response.toSubscribeLevel1Model() = CoinModel(
    InstrumentId = InstrumentId,
    LastTradedPx = LastTradedPx,
    Rolling24HrVolume = Rolling24HrVolume,
    Rolling24HrPxChange = Rolling24HrPxChange
)