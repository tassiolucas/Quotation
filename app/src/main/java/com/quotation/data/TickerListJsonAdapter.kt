package com.quotation.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quotation.data.entity.Coin
import com.quotation.data.entity.TickerList
import com.squareup.moshi.*

class TickerListJsonAdapter() : JsonAdapter<TickerList>() {

    @FromJson
    override fun fromJson(reader: JsonReader): TickerList {
        var m: Int? = null
        var i: Int? = null
        var n: String? = null
        var o: List<Coin>? = null
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "m" -> m = reader.nextInt()
                "i" -> i = reader.nextInt()
                "n" -> n = reader.nextString()
                "o" -> {
                    val type = object : TypeToken<List<Coin>>() {}.type
                    o = Gson().fromJson(reader.nextString(), type)
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return TickerList(
            m = m ?: 0,
            i = i ?: 0,
            n = n ?: "",
            o = o ?: listOf(Coin())
        )
    }

    override fun toJson(writer: JsonWriter, value: TickerList?) {
        writer.beginObject()
        writer.name("m").value(value?.m)
        writer.name("i").value(value?.i)
        writer.name("n").value(value?.n)
        val type = object : TypeToken<List<Coin>>() {}.type
        writer.name("o").value(Gson().toJson(writer, type))
        writer.endObject()
    }
}