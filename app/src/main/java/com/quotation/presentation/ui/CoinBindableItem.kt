package com.quotation.presentation.ui

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.quotation.R
import com.example.quotation.databinding.ItemCoinBinding
import com.quotation.data.formatVariation
import com.quotation.data.formatVariationColor
import com.quotation.data.toBRLCurrency
import com.quotation.presentation.ui.base.BaseBindableItem

class CoinBindableItem(private val owner: LifecycleOwner, private val config: Config) :
    BaseBindableItem<ItemCoinBinding>() {

    val index = config.index
    val currencyTitleValue = MutableLiveData<Number>()
    val variationTitleValue = MutableLiveData<Number>()

    override fun initializeViewBinding(view: View) = ItemCoinBinding.bind(view)

    override fun bind(viewBinding: ItemCoinBinding, position: Int) {
        with(viewBinding) {
            tvValueTitle.visibility = INVISIBLE
            tvCoinVariation.visibility = INVISIBLE
            pgValueTitle.visibility = VISIBLE
            pgCoinVariation.visibility = VISIBLE

            config.imageRes?.let {
                civCoinImage.setImageResource(it)
            }

            tvCoinTitle.text = config.nameTitle

            tvCoinSymbol.text = config.symbolTitle

            currencyTitleValue.observe(owner) {
                tvValueTitle.text = it?.toBRLCurrency()
                tvValueTitle.visibility = VISIBLE
                pgValueTitle.visibility = INVISIBLE
            }

            variationTitleValue.observe(owner) {
                tvCoinVariation.setTextColor(it.formatVariationColor(root.context))
                tvCoinVariation.text = it.formatVariation()
                tvCoinVariation.visibility = VISIBLE
                pgCoinVariation.visibility = INVISIBLE
            }
        }
    }

    override fun getLayout() = R.layout.item_coin

    data class Config(
        @DrawableRes val imageRes: Int? = null,
        val index: Int,
        val nameTitle: String,
        val symbolTitle: String,
    )
}