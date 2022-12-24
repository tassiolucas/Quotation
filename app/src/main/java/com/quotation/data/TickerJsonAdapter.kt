package com.quotation.data

import com.google.gson.Gson
import com.quotation.data.entity.Coin
import com.quotation.data.entity.Ticker
import com.squareup.moshi.*

class TickerJsonAdapter : JsonAdapter<Ticker>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Ticker {
        var m: Int? = null
        var i: Int? = null
        var n: String? = null
        var o: Coin? = null
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "m" -> m = reader.nextInt()
                "i" -> i = reader.nextInt()
                "n" -> n = reader.nextString()
                "o" -> o = Gson().fromJson(reader.nextString(), Coin::class.java)
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Ticker(
            m = m ?: 0,
            i = i ?: 0,
            n = n ?: "",
            o = o ?: Coin()
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Ticker?) {
        writer.beginObject()
        writer.name("m").value(value?.m)
        writer.name("i").value(value?.i)
        writer.name("n").value(value?.n)
        writer.name("o").value(Gson().toJson(value?.o, Coin::class.java))
        writer.endObject()
    }
}