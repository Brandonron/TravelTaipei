package com.example.traveltaipei.fragment

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
import com.example.traveltaipei.R
import com.example.traveltaipei.adapter.FooterAdapter
import com.example.traveltaipei.data.events.NewsBody
import com.example.traveltaipei.databinding.FragmentNewsListBinding
import com.example.traveltaipei.databinding.ItemNewsListBinding
import com.example.traveltaipei.viewmodel.DataViewModel
import com.example.traveltaipei.viewmodel.ServiceViewModel
import com.example.traveltaipei.viewmodel.WebViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsListFragment : Fragment() {

    private lateinit var binding: FragmentNewsListBinding

    private val apiModel: ServiceViewModel by viewModel()

    private lateinit var webModel: WebViewModel
    private lateinit var dataModel: DataViewModel

    private val itemListAdapter: ItemListAdapter by lazy { ItemListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[WebViewModel::class.java]

        dataModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DataViewModel::class.java]

        dataModel.languageCode.observe(viewLifecycleOwner) {
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

        callApi(dataModel.languageCode.value ?: DataViewModel.LangType.TW)
    }

    private fun callApi(lang: String) {
        lifecycleScope.launch {
            apiModel.callNews(lang).collect { pagingData ->
                itemListAdapter.submitData(pagingData)
            }
        }
    }

    inner class ItemListAdapter :
        PagingDataAdapter<NewsBody, ItemListAdapter.ItemViewHolder>(object :
            DiffUtil.ItemCallback<NewsBody>() {
            override fun areItemsTheSame(oldItem: NewsBody, newItem: NewsBody): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: NewsBody,
                newItem: NewsBody
            ): Boolean {
                return oldItem.description == newItem.description
            }
        }) {

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemBinding =
                ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val data = getItem(position)
            holder.bind(data)
        }

        inner class ItemViewHolder(private val binding: ItemNewsListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(data: NewsBody?) {
                if (data == null) {
                    return
                }
                binding.tvTitle.text = data.title
                binding.tvDescription.text = data.description
                binding.root.setOnClickListener {
                    dataModel.updateToolbarTitle("最新消息")
                    Navigation.findNavController(it)
                        .navigate(R.id.action_travelFragment_to_webFragment)
                    webModel.updateURL(data?.url)
                }
            }
        }
    }
}