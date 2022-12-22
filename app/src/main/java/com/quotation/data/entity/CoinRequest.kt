package com.quotation.data.entity

import com.quotation.domain.entities.Coin
import com.squareup.moshi.Json

data class CoinRequest(
    override val m: Int,
    override val i: Int,
    override val n: String,
    override val o: String
): BaseCoinRequest(m, i, n, o)

fun Coin.toCoinRequest() = CoinRequest(
    m, i, n, o
)

data class CoinDataRequest(
    @Json(name = "InstrumentId")
    val InstrumentId: Int,
)





