package com.quotation.presentation.ui.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.Item

abstract class BaseBindableItem<T : ViewBinding> : Item<BaseViewHolder<T>>() {

    protected abstract fun initializeViewBinding(view: View): T

    override fun createViewHolder(itemView: View): BaseViewHolder<T> =
        BaseViewHolder(initializeViewBinding(itemView))

    override fun bind(viewHolder: BaseViewHolder<T>, position: Int) {
        throw RuntimeException("Doesn't get called")
    }

    override fun bind(viewHolder: BaseViewHolder<T>, position: Int, payloads: List<Any>) {
        bind(viewHolder.binding, position, payloads)
    }

    abstract fun bind(viewBinding: T, position: Int)

    fun bind(viewBinding: T, position: Int, payloads: List<Any>) {
        bind(viewBinding, position)
    }
}