package com.quotation.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Subscribe(
    @Json(name = "m")
    val m: Int,
    @Json(name = "i")
    val i: Int,
    @Json(name = "n")
    val n: String,
    @Json(name = "o")
    val o: String
)