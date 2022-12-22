package com.quotation.data.entity

import com.squareup.moshi.Json

abstract class BaseCoinRequest(
    @Json(name = "m")
    open val m: Int,
    @Json(name = "i")
    open val i: Int,
    @Json(name = "n")
    open val n: String,
    @Json(name = "o")
    open val o: String
)