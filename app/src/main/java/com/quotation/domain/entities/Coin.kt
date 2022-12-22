package com.quotation.domain.entities

class Coin(
    override val m: Int,
    override val i: Int,
    override val n: String,
    override val o: String
) : BaseSubscribe(m, i, n, o)