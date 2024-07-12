package com.example.e_commerce.shopping_feature.presentation.fragments


import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.core.fragments.BaseFragment
import com.example.e_commerce.core.utils.DepthPageTransformer
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.core.views.CircleView
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.shopping_feature.presentation.adapters.CategoriesAdapter
import com.example.e_commerce.shopping_feature.presentation.adapters.SalesAdAdapter
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel
import com.example.e_commerce.shopping_feature.presentation.models.SalesAdUIModel
import com.example.e_commerce.shopping_feature.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment  : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun init() {
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


    private fun initSalesAdsView(salesAds: List<SalesAdUIModel>?) {
        if (salesAds.isNullOrEmpty()) {
            return
        }

        initializeIndicators(salesAds.size)
        val salesAdapter = SalesAdAdapter(lifecycleScope, salesAds)
        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(position)
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

        // add animation from top to bottom
        binding.saleAdsViewPager.animate().translationY(0f).alpha(1f).setDuration(500).start()

    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private var indicators = mutableListOf<CircleView>()

    private fun initializeIndicators(count: Int) {
        for (i in 0 until count) {
            val circleView = CircleView(requireContext())
            val params = LinearLayout.LayoutParams(
                20, 20
            )
            params.setMargins(8, 0, 8, 0) // Margin between circles
            circleView.layoutParams = params
            circleView.setRadius(10f) // Set radius
            circleView.setColor(
                if (i == 0) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            ) // First indicator is red
            circleView.setOnClickListener {
                binding.saleAdsViewPager.setCurrentItem(i, true)
            }
            indicators.add(circleView)
            binding.indicatorView.addView(circleView)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicators.size) {
            indicators[i].setColor(
                if (i == position) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            )
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

    companion object {
        private const val TAG = "HomeFragment"
    }
}