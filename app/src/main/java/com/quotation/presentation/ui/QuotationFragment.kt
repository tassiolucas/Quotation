package com.quotation.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotation.databinding.FragmentQuotationBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuotationFragment : Fragment() {

    private val viewModel: QuotationViewModel by viewModel()

    private lateinit var binding: FragmentQuotationBinding

    private val coinsListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentQuotationBinding.inflate(inflater).apply {
        binding = this
    }.root

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCoins.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinsListAdapter
        }

        viewModel.startCoinsList(viewLifecycleOwner)

        viewModel.coinList.observe(viewLifecycleOwner) {
            viewModel.loadCoinsList(it)
        }

        viewModel.updateEvent.observe(viewLifecycleOwner) {
            coinsListAdapter.update(viewModel.coinBindableItems)
        }
    }

}