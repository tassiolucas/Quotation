package com.quotation.data

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.quotation.R
import java.text.NumberFormat
import java.util.*

enum class InstrumentId(val id: Int, @DrawableRes val imgRes: Int, val coinName: String) {
    BTC(1, R.drawable.btc, "Bitcoin"),
    XRP(10, R.drawable.xrp, "XRP"),
    TUSD(6, R.drawable.tusd, "TrueUSD"),
    ETH(4, R.drawable.eth, "Ethereum"),
    LTC(2, R.drawable.ltc, "Litecoin");

    companion object {
        fun getPosition(id: Int) = values().find { it.id == id }?.ordinal ?: 1

        fun getImageRes(id: Int) = values().find { it.id == id }?.imgRes

        fun getCoinName(id: Int) = values().find { it.id == id }?.coinName ?: ""
    }
}

fun Number.toBRLCurrency(): String {
    val locale = Locale("bt", "BR")
    return NumberFormat.getCurrencyInstance(locale).format(this)
}

fun Number.formatVariation(): String = if (this.toFloat() > 0.0) "+${this}" else "$this"

fun Number.formatVariationColor(context: Context): Int =
    if (this.toFloat() > 0.0) ContextCompat.getColor(context, R.color.positive)
    else ContextCompat.getColor(context, R.color.negative)