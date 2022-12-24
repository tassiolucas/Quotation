package com.quotation.data.entity

import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("InstrumentId")
    val instrumentId: Int? = null,
    @SerializedName("Symbol")
    val symbol: String? = null,
    @SerializedName("SortIndex")
    val sortIndex: Int? = null,
    @SerializedName("LastTradedPx")
    val lastTradedPx: Number? = null,
    @SerializedName("Rolling24HrVolume")
    val rolling24HrVolume: Number? = null,
    @SerializedName("Rolling24HrPxChange")
    val rolling24HrPxChange: Number? = null
)