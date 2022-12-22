package com.quotation.data.entity

import com.squareup.moshi.*

data class BaseCoinResponse(
    @Json(name = "m")
    val m: Int,
    @Json(name = "i")
    val i: Int,
    @Json(name = "n")
    val n: String,
    @Json(name = "o")
    val o: String
)

data class GetInstrumentIdResponse(
    @Json(name = "InstrumentId")
    val InstrumentId: Int,
    @Json(name = "Symbol")
    val Symbol: String,
    @Json(name = "SortIndex")
    val SortIndex: Int
)

data class SubscribeLevel1Response(
    @Json(name = "InstrumentId")
    val InstrumentId: Int,
    @Json(name = "LastTradedPx")
    val LastTradedPx: Number,
    @Json(name = "Rolling24HrVolume")
    val Rolling24HrVolume: Number,
    @Json(name = "Rolling24HrPxChange")
    val Rolling24HrPxChange: Number
)


