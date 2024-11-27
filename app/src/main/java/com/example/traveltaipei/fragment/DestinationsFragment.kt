package com.example.traveltaipei.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.traveltaipei.R
import com.example.traveltaipei.data.attractions.Image
import com.example.traveltaipei.databinding.FragmentDestinationsBinding
import com.example.traveltaipei.databinding.ItemImageBinding
import com.example.traveltaipei.viewmodel.DataViewModel
import com.example.traveltaipei.viewmodel.WebViewModel


class DestinationsFragment : Fragment() {

    private lateinit var binding: FragmentDestinationsBinding

    private lateinit var webModel: WebViewModel
    private lateinit var dataModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

        webModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[WebViewModel::class.java]

        dataModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DataViewModel::class.java]

        dataModel.selectDestinationsData.value?.let { data ->
            dataModel.updateToolbarTitle(data.name)

            binding.recyclerView.apply {
                adapter = ImageAdapter(data.images)
            }

            binding.tvOpenTime.text = "營業時間:${data.open_time}"
            binding.tvAddress.text = "地址:${data.address}"
            binding.tvPhone.text = "電話:${data.tel}"

            binding.tvUrl.text = "網址:${data.url}"
            binding.tvUrl.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_destinationsFragment_to_webFragment)
                webModel.updateURL(data.url)
            }

            binding.tvDescription.text = data.introduction
        }
    }

    inner class ImageAdapter(private val mData: List<Image>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        var maxDisplayWidth = 0
        var maxDisplayHeight = 0
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)

            PagerSnapHelper().apply {
                attachToRecyclerView(recyclerView)
            }

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

            maxDisplayWidth = (displayMetrics.widthPixels * 0.9).toInt()
            maxDisplayHeight =  (displayMetrics.widthPixels * 0.5).toInt()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ImageAdapter.ImageViewHolder {
            val itemBinding =
                ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageViewHolder(itemBinding)
        }

        override fun getItemCount(): Int {
            return mData.size
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val data = mData[position]
            holder.bind(data)
        }

        inner class ImageViewHolder(private val binding: ItemImageBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("CheckResult")
            fun bind(data: Image) {

                Glide.with(this@DestinationsFragment)
                    .load(data.src)
                    .apply(RequestOptions().apply {
                        placeholder(R.drawable.baseline_image_24)
                        error(R.drawable.baseline_image_24)
                        override(300, 300)
                    })
                    .into(binding.photo.apply {
                        layoutParams.apply {
                            width = maxDisplayWidth
                            height = maxDisplayHeight
                        }
                        scaleType = ImageView.ScaleType.FIT_XY
                    })
            }
        }
    }
}