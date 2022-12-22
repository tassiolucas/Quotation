package com.quotation.presentation.ui.base

import androidx.viewbinding.ViewBinding
import com.xwray.groupie.GroupieViewHolder

class BaseViewHolder<T : ViewBinding>(val binding: T) : GroupieViewHolder(
    binding.root
)