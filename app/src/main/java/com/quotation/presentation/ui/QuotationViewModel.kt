package com.quotation.presentation.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quotation.data.InstrumentId
import com.quotation.data.InstrumentId.*
import com.quotation.data.entity.Coin
import com.quotation.data.getCoins
import com.quotation.data.toJson
import com.quotation.domain.entities.BaseSubscribe
import com.quotation.domain.usecase.ObserveTickerListUseCase
import com.quotation.domain.usecase.ObserveTickerUseCase
import com.quotation.domain.usecase.SendSubscribeUseCase
import com.quotation.domain.usecase.WebSocketUseCase
import com.quotation.domain.usecase.base.UseCase.None
import com.quotation.ext.Executors
import com.quotation.ext.applySchedulers
import com.quotation.ext.w
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

class QuotationViewModel(
    private val webSocketUseCase: WebSocketUseCase,
    private val observeTickerUseCase: ObserveTickerUseCase,
    private val observeTickerListUseCase: ObserveTickerListUseCase,
    private val sendSubscribeUseCase: SendSubscribeUseCase,
    private val executors: Executors
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _coinList = MutableLiveData<List<Coin>>()
    val coinList: LiveData<List<Coin>> = _coinList

    private val _updateEvent = MutableLiveData<Unit>()
    val updateEvent get(): LiveData<Unit> = _updateEvent

    val coinBindableItems = mutableListOf<CoinBindableItem>()

    private var start = false
    private val timer = Timer()

    fun startCoinsList(lifecycleOwner: LifecycleOwner) {

        webSocketUseCase.execute(None()).applySchedulers(executors)
            .subscribe({
                if (!start) {
                    Timber.tag("CoinLog").w("Ticker")
                    sendSubscribeUseCase.execute(GET_COINS_PARAMS)
                    start = true
                    timer.schedule(TICKER_TIME) {
                        start = false
                    }
                }
            },
                { w { it.localizedMessage?.toString().toString() } }).addTo(disposables)

        observeTickerListUseCase.execute(None()).applySchedulers(executors).subscribe({ ticker ->
            if (ticker.o[0].instrumentId != null) {
                setupCoinList(lifecycleOwner, ticker.getCoins())
            }
        }, { w { it.localizedMessage?.toString().toString() } }).addTo(disposables)
    }

    private fun setupCoinList(
        lifecycleOwner: LifecycleOwner, coinList: List<Coin>
    ) {
        if (coinBindableItems.isEmpty()) {
            val items = coinList.filter {
                it.instrumentId == BTC.id ||
                it.instrumentId == XRP.id ||
                it.instrumentId == TUSD.id ||
                it.instrumentId == ETH.id ||
                it.instrumentId == LTC.id
            }.map { coin ->
                val item = CoinBindableItem(
                    owner = lifecycleOwner, config = CoinBindableItem.Config(
                        index = InstrumentId.getPosition(coin.instrumentId),
                        imageRes = InstrumentId.getImageRes(coin.instrumentId),
                        nameTitle = InstrumentId.getCoinName(coin.instrumentId),
                        symbolTitle = coin.symbol
                    )
                )

                coinBindableItems.add(item)
                coin
            }

            _coinList.postValue(items)
            coinBindableItems.sortBy { it.index }
        }

        _updateEvent.postValue(Unit)
    }

    fun loadCoinsList(coinList: List<Coin>) {
        coinList.map { coin ->
            sendSubscribeUseCase.execute(
                SendSubscribeUseCase.SubscribeParams(
                    n = BaseSubscribe.SUBSCRIBE_LEVEL_1,
                    o = coin.toJson()
                )
            )
        }

        observeTickerUseCase.execute(None()).applySchedulers(executors).subscribe({ ticker ->
            updateCoinsMap(ticker.o)
            Timber.tag("CoinLog").w("Coin: ${ticker.o.instrumentId}")
        }, { w { it.localizedMessage?.toString().toString() } }).addTo(disposables)
    }

    private fun updateCoinsMap(coin: Coin) {
        coinBindableItems[InstrumentId.getPosition(coin.instrumentId)].currencyTitleValue.postValue(
            coin.lastTradedPx
        )
        coinBindableItems[InstrumentId.getPosition(coin.instrumentId)].variationTitleValue.postValue(
            coin.rolling24HrPxChange
        )

        _updateEvent.postValue(Unit)
    }

    companion object {
        private val GET_COINS_PARAMS = SendSubscribeUseCase.SubscribeParams(
            n = BaseSubscribe.GET_INSTRUMENTS,
            o = Coin().toJson()
        )
        private const val TICKER_TIME = 3000L
    }
}