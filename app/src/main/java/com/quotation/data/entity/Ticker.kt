package com.quotation.data.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.*
import com.fasterxml.jackson.annotation.JsonInclude.Include.*
import com.squareup.moshi.Json

data class Ticker(
    @JsonInclude(NON_NULL)
    @Json(name = "m")
    val m: Int,
    @JsonInclude(NON_NULL)
    @Json(name = "i")
    val i: Int,
    @JsonInclude(NON_NULL)
    @Json(name = "n")
    val n: String,
    @JsonInclude(NON_NULL)
    @Json(name = "o")
    val o: Coin
)