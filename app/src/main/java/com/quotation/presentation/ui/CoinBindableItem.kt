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
import com.quotation.presentation.ui.base.BaseViewHolder

class CoinBindableItem(private val config: Config) :
    BaseBindableItem<ItemCoinBinding>() {

    private lateinit var owner: LifecycleOwner
    val index = config.index
    val currencyTitleValue = MutableLiveData<Number>()
    val variationTitleValue = MutableLiveData<Number>()

    override fun initializeViewBinding(view: View) = ItemCoinBinding.bind(view)

    override fun bind(viewBinding: ItemCoinBinding, position: Int) {
        with(viewBinding) {
            owner = viewBinding.root.context as LifecycleOwner

            toggleLoading(viewBinding, true)

            config.imageRes?.let {
                civCoinImage.setImageResource(it)
            }

            tvCoinTitle.text = config.nameTitle

            tvCoinSymbol.text = config.symbolTitle

            currencyTitleValue.observe(owner) {
                tvValueTitle.text = it?.toBRLCurrency()
                toggleLoading(this, false)
            }

            variationTitleValue.observe(owner) {
                tvCoinVariation.setTextColor(it.formatVariationColor(root.context))
                tvCoinVariation.text = it.formatVariation()
                toggleLoading(this, false)
            }
        }
    }

    private fun toggleLoading(viewBinding: ItemCoinBinding, isLoading: Boolean) {
        with(viewBinding) {
            if (isLoading) {
                tvValueTitle.visibility = INVISIBLE
                tvCoinVariation.visibility = INVISIBLE
                pgValueTitle.visibility = VISIBLE
                pgCoinVariation.visibility = VISIBLE
            } else {
                tvValueTitle.visibility = VISIBLE
                tvCoinVariation.visibility = VISIBLE
                pgValueTitle.visibility = INVISIBLE
                pgCoinVariation.visibility = INVISIBLE
            }
        }
    }

    override fun onViewDetachedFromWindow(viewHolder: BaseViewHolder<ItemCoinBinding>) {
        currencyTitleValue.removeObservers(owner)
        variationTitleValue.removeObservers(owner)
        super.onViewDetachedFromWindow(viewHolder)
    }

    override fun getLayout() = R.layout.item_coin

    data class Config(
        @DrawableRes val imageRes: Int? = null,
        val index: Int,
        val nameTitle: String,
        val symbolTitle: String?,
    )
}