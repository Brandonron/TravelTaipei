package com.example.traveltaipei.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.traveltaipei.databinding.FragmentTravelBinding
import com.example.traveltaipei.viewmodel.DataViewModel
import com.google.android.material.tabs.TabLayoutMediator


class TravelFragment : Fragment() {

    private lateinit var binding: FragmentTravelBinding

    private lateinit var model: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DataViewModel::class.java]
        model.updateToolbarTitle("悠遊台北")

        activity?.let {
            val pagerAdapter = SlidePagerAdapter(it)
            binding.pager.adapter = pagerAdapter
            binding.pager.setPageTransformer { page, position ->
                // Apply desired animations here
            }

            TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
                tab.text = pagerAdapter.getPageTitle(position)
                binding.pager.setCurrentItem(tab.position, true)
            }.attach()
        }
    }

    inner class SlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        private var fragmentList = listOf(NewsListFragment(), DestinationsListFragment())
        private var fragmentTitles = listOf("最新消息", "旅遊景點")

        override fun getItemCount(): Int = fragmentList.size // Total number of pages
        override fun createFragment(position: Int): Fragment {
            // Return a new fragment instance based on position
            return fragmentList[position]
        }

        fun getPageTitle(position: Int): String {
            return fragmentTitles[position]
        }
    }
}