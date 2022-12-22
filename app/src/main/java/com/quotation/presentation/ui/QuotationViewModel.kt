package com.quotation.presentation.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quotation.data.InstrumentId
import com.quotation.domain.model.CoinModel
import com.quotation.domain.usecase.ObserveGetInstrumentUseCase
import com.quotation.domain.usecase.ObserverSubscribeLevel1UseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class QuotationViewModel(
    private val observerGetInstrumentUseCase: ObserveGetInstrumentUseCase,
    private val observerSubscribeLevel1UseCase: ObserverSubscribeLevel1UseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _coinList = MutableLiveData<List<CoinModel>>()
    val coinList: LiveData<List<CoinModel>> = _coinList

    val coinBindableItems = mutableListOf<CoinBindableItem>()

    fun startCoinsList(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        compositeDisposable.add(
            observerGetInstrumentUseCase.invoke()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getList ->
                    if (coinBindableItems.isEmpty()) {
                        getList.map { coin ->
                            val item = CoinBindableItem(
                                owner = lifecycleOwner,
                                config = CoinBindableItem.Config(
                                    index = InstrumentId.getPosition(coin.InstrumentId),
                                    imageRes = InstrumentId.getImageRes(coin.InstrumentId),
                                    nameTitle = InstrumentId.getCoinName(coin.InstrumentId),
                                    symbolTitle = coin.Symbol
                                )
                            )

                            coinBindableItems.add(item)
                            coinBindableItems.sortBy { it.index }
                        }
                        onUpdate.invoke()
                        _coinList.value = getList
                    }
                }, { e ->
                    Timber.e("observerCoinUseCase error: %s", e.toString())
                })
        )
    }

    fun loadCoinsList(coinList: List<CoinModel>, onUpdate: () -> Unit) {
        coinList.map { coin ->
            compositeDisposable.add(
                observerSubscribeLevel1UseCase.invoke(coin.InstrumentId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ coinData ->
                        val model = coinList.find {
                            it.InstrumentId == coinData.InstrumentId
                        }?.copy(
                            LastTradedPx = coinData.LastTradedPx,
                            Rolling24HrVolume = coinData.Rolling24HrVolume,
                            Rolling24HrPxChange = coinData.Rolling24HrPxChange
                        )

                        model?.let {
                            updateCoinsMap(model, onUpdate)
                        }
                    }, { e ->
                        Timber.e("observerSubscribeLevel1UseCase error: %s", e.toString())
                    })
            )
        }
    }

    private fun updateCoinsMap(coin: CoinModel, onUpdate: () -> Unit) {
        coinBindableItems[InstrumentId.getPosition(coin.InstrumentId)].currencyTitleValue.value =
            coin.LastTradedPx
        coinBindableItems[InstrumentId.getPosition(coin.InstrumentId)].variationTitleValue.value =
            coin.Rolling24HrPxChange

        onUpdate.invoke()
    }

}