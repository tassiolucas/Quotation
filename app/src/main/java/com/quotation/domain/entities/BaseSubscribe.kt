package com.quotation.domain.entities

abstract class BaseSubscribe(
    open val m: Int,
    open val i: Int,
    open val n: String,
    open val o: String
) {
    companion object {
        const val M = 0
        const val I = 0
        const val GET_INSTRUMENTS = "getInstruments"
        const val SUBSCRIBE_LEVEL_1 = "SubscribeLevel1"
    }
}