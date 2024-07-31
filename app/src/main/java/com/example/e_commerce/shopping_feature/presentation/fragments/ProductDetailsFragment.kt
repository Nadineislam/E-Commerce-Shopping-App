package com.example.e_commerce.shopping_feature.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.core.fragments.BaseFragment
import com.example.e_commerce.core.utils.DepthPageTransformer
import com.example.e_commerce.core.views.CircleView
import com.example.e_commerce.core.views.sliderIndicatorsView
import com.example.e_commerce.core.views.updateIndicators
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.shopping_feature.presentation.adapters.ProductImagesAdapter
import com.example.e_commerce.shopping_feature.presentation.models.ProductUIModel
import com.example.e_commerce.shopping_feature.presentation.viewmodel.ProductDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>() {

    override val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_product_details

    override fun init() {
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
                initView(it)
            }
        }
    }

    private fun initView(it: ProductUIModel) {
        it.name.let { binding.titleTv.text = it }
        initImagesView(it.images)
    }

    private var indicators = mutableListOf<CircleView>()
    private fun initImagesView(images: List<String>) {

        sliderIndicatorsView(
            requireContext(),
            binding.productImagesViewPager,
            binding.indicatorView,
            indicators,
            images.size
        )
        binding.productImagesViewPager.apply {
            adapter = ProductImagesAdapter(images)
            setPageTransformer(DepthPageTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }
    }
}