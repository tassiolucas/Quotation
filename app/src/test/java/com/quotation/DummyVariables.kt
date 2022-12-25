package com.quotation

import com.quotation.data.entity.Coin
import com.quotation.data.entity.Subscribe
import com.quotation.data.entity.Ticker
import com.quotation.data.entity.TickerList
import com.quotation.data.toJson
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.usecase.SendSubscribeUseCase
import com.quotation.presentation.ui.CoinBindableItem

val dummyCoin = Coin(
    instrumentId = 1,
    symbol = "BTC",
    sortIndex = 1,
    lastTradedPx = 1.0,
    rolling24HrVolume = 1.0,
    rolling24HrPxChange = 1.0,
)

val dummyTicker = Ticker(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = Coin()
)

val dummyTickerList = TickerList(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = listOf(dummyCoin)
)

val dummyCoinBindableItem = CoinBindableItem(
    config = CoinBindableItem.Config(
        imageRes = 0,
        index = 0,
        nameTitle = "Bitcoin",
        symbolTitle = "BTC",
    )
)

val dummySubscribe = Subscribe(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = Coin().toJson()
)

val dummyGetInstrumentsParams = SendSubscribeUseCase.SubscribeParams(
    m = BaseSubscribe.M,
    i = BaseSubscribe.I,
    n = BaseSubscribe.GET_INSTRUMENTS,
    o = Coin().toJson()
)

val dummySubscribeLevel1Params = SendSubscribeUseCase.SubscribeParams(
    n = BaseSubscribe.SUBSCRIBE_LEVEL_1,
    o = Coin(
        instrumentId = 1
    ).toJson()
)

