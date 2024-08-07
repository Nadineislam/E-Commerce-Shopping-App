package com.example.e_commerce.shopping_feature.presentation.fragments


import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.core.fragments.BaseFragment
import com.example.e_commerce.core.utils.DepthPageTransformer
import com.example.e_commerce.core.utils.GridSpacingItemDecoration
import com.example.e_commerce.core.utils.HorizontalSpacingItemDecoration
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.core.views.CircleView
import com.example.e_commerce.core.views.loadImage
import com.example.e_commerce.core.views.sliderIndicatorsView
import com.example.e_commerce.core.views.updateIndicators
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.shopping_feature.presentation.activity.ProductDetailsActivity
import com.example.e_commerce.shopping_feature.presentation.activity.ProductDetailsActivity.Companion.PRODUCT_UI_MODEL_EXTRA
import com.example.e_commerce.shopping_feature.presentation.adapters.CategoriesAdapter
import com.example.e_commerce.shopping_feature.presentation.adapters.ProductAdapter
import com.example.e_commerce.shopping_feature.presentation.adapters.ProductViewType
import com.example.e_commerce.shopping_feature.presentation.adapters.SalesAdAdapter
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import com.example.e_commerce.shopping_feature.presentation.models.ProductUIModel
import com.example.e_commerce.shopping_feature.presentation.models.SalesAdUIModel
import com.example.e_commerce.shopping_feature.presentation.models.SpecialSectionUIModel
import com.example.e_commerce.shopping_feature.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_home

    private var indicators = mutableListOf<CircleView>()

    override fun init() {
        initViews()
        iniViewModel()
    }

    private fun iniViewModel() {
        lifecycleScope.launch {
            viewModel.salesAdsState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: Loading")
                    }

                    is Resource.Success -> {
                        binding.saleAdsShimmerView.root.stopShimmer()
                        binding.saleAdsShimmerView.root.visibility = View.GONE
                        initSalesAdsView(resources.data)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: Error")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.categoriesState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: categories Loading")
                    }

                    is Resource.Success -> {
//                        binding.categoriesShimmerView.root.stopShimmer()
//                        binding.categoriesShimmerView.root.visibility = View.GONE
                        Log.d(TAG, "iniViewModel: categories Success = ${resources.data}")
                        initCategoriesView(resources.data)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: categories Error")
                    }
                }
            }
        }

//        viewModel.getFlashSaleProducts()

        lifecycleScope.launch {
            viewModel.flashSaleState.collect { productsList ->
                flashSaleAdapter.submitList(productsList)
                Log.d(TAG, "iniViewModel: flashSaleState = $productsList")
                binding.invalidateAll()
            }
        }
        lifecycleScope.launch {
            viewModel.megaSaleState.collect { productsList ->
                megaSaleAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }
        lifecycleScope.launch {
            viewModel.recommendedSectionDataState.collectLatest { recommendedSectionData ->
                Log.d(TAG, "Recommended section data: $recommendedSectionData")
                recommendedSectionData?.let {
                    setupRecommendedViewData(it)
                } ?: run {
                    Log.d(TAG, "Recommended section data is null")
//                    binding.recommendedProductLayout.visibility = View.GONE
                }
            }
        }
        viewModel.getNextProducts()
        lifecycleScope.launch {
            viewModel.allProductsState.collectLatest { productsList ->
                allProductsAdapter.submitList(productsList)
                Log.d(TAG, "iniViewModel: allProductsState = $productsList")
                binding.invalidateAll()
            }
        }


    }

    private fun setupRecommendedViewData(sectionData: SpecialSectionUIModel) {
        loadImage(binding.recommendedProductIv, sectionData.image)
        binding.recommendedProductTitleIv.text = sectionData.title
        binding.recommendedProductDescriptionIv.text = sectionData.description
        binding.recommendedProductLayout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Recommended Product Clicked, goto ${sectionData.type}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initCategoriesView(data: List<CategoryUIModel>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val categoriesAdapter = CategoriesAdapter(data)
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private val flashSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val megaSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val allProductsAdapter by lazy { ProductAdapter { goToProductDetails(it) } }

    private fun goToProductDetails(product: ProductUIModel) {
        requireActivity().startActivity(
            Intent(
                requireActivity(), ProductDetailsActivity::class.java
            ).apply {
                putExtra(PRODUCT_UI_MODEL_EXTRA, product)
            })
    }

    private fun initViews() {
        binding.flashSaleProductsRv.apply {
            adapter = flashSaleAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            addItemDecoration(HorizontalSpacingItemDecoration(16))
        }
        binding.megaSaleProductsRv.apply {
            adapter = megaSaleAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            addItemDecoration(HorizontalSpacingItemDecoration(16))
        }
        binding.allProductsRv.apply {
            adapter = allProductsAdapter
            layoutManager = GridLayoutManager(
                requireContext(), 2
            )
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }
    }

    private fun initSalesAdsView(salesAds: List<SalesAdUIModel>?) {
        if (salesAds.isNullOrEmpty()) {
            return
        }

        sliderIndicatorsView(
            requireContext(),
            binding.saleAdsViewPager,
            binding.indicatorView,
            indicators,
            salesAds.size
        )
        val salesAdapter = SalesAdAdapter(lifecycleScope, salesAds)
        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }

        lifecycleScope.launch(IO) {
            tickerFlow(5000).collect {
                withContext(Main) {
                    binding.saleAdsViewPager.setCurrentItem(
                        (binding.saleAdsViewPager.currentItem + 1) % salesAds.size, true
                    )
                }
            }
        }

        binding.saleAdsViewPager.animate().translationY(0f).alpha(1f).setDuration(500).start()

    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }
}