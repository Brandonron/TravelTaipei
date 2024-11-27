package com.example.traveltaipei.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traveltaipei.databinding.ItemFooterBinding

class FooterAdapter (private val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.FooterViewHolder>() {

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState, retry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        return FooterViewHolder(ItemFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        )
    }

    class FooterViewHolder(private val binding: ItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState, retry: () -> Unit) {
            binding.retryButton.setOnClickListener {
                retry()
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
        }
    }
}