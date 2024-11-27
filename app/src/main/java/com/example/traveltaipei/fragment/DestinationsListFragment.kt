package com.example.traveltaipei.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.traveltaipei.R
import com.example.traveltaipei.adapter.FooterAdapter
import com.example.traveltaipei.data.attractions.TravelAllBody
import com.example.traveltaipei.databinding.FragmentDestinationsListBinding
import com.example.traveltaipei.databinding.ItemDestinationsListBinding
import com.example.traveltaipei.viewmodel.DataViewModel
import com.example.traveltaipei.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DestinationsListFragment : Fragment() {

    private lateinit var binding: FragmentDestinationsListBinding

    private val apiModel: ServiceViewModel by viewModel()
    private lateinit var model: DataViewModel

    private val itemListAdapter: ItemListAdapter by lazy { ItemListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DataViewModel::class.java]

        model.languageCode.observe(viewLifecycleOwner) {
            callApi(it ?: DataViewModel.LangType.TW)
        }

        binding.recyclerView.adapter = itemListAdapter.apply {
            withLoadStateFooter(FooterAdapter { retry() })

            addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.viewEmpty.isVisible = (this.itemCount < 1)
                    }

                    is LoadState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.viewEmpty.visibility = View.INVISIBLE
                    }

                    is LoadState.Error -> {
                        val state = it.refresh as LoadState.Error
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.viewEmpty.isVisible = (this.itemCount < 1)
                        Toast.makeText(
                            context,
                            "加載失敗: ${state.error.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        callApi(model.languageCode.value ?: DataViewModel.LangType.TW)
    }

    private fun callApi(lang: String) {
        lifecycleScope.launch {
            apiModel.callTravelAll(lang).collect { pagingData ->
                itemListAdapter.submitData(pagingData)
            }
        }
    }

    inner class ItemListAdapter :
        PagingDataAdapter<TravelAllBody, ItemListAdapter.ItemViewHolder>(object :
            DiffUtil.ItemCallback<TravelAllBody>() {
            override fun areItemsTheSame(oldItem: TravelAllBody, newItem: TravelAllBody): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: TravelAllBody,
                newItem: TravelAllBody
            ): Boolean {
                return oldItem.introduction == newItem.introduction
            }

        }) {

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemBinding =
                ItemDestinationsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ItemViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val data = getItem(position)
            holder.bind(data)
        }

        inner class ItemViewHolder(private val binding: ItemDestinationsListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("CheckResult")
            fun bind(data: TravelAllBody?) {
                if (data == null) {
                    return
                }

                Glide.with(this@DestinationsListFragment)
                    .load(data.images.firstOrNull()?.src)
                    .apply(RequestOptions().apply {
                        placeholder(R.drawable.baseline_image_24)
                        error(R.drawable.baseline_image_24)
                        override(300, 300)
                    })
                    .into(binding.imSrc)

                binding.tvName.text = data.name
                binding.tvDescription.text = data.introduction

                binding.root.setOnClickListener {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_travelFragment_to_destinationsFragment)

                    model.updateToolbarTitle(data?.name)
                    model.updateDestinationsData(data)
                }
            }
        }
    }
}