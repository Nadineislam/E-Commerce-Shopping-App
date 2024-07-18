package com.example.e_commerce.shopping_feature.presentation.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.auth_feature.data.user.CountryData
import com.example.e_commerce.auth_feature.domain.repository.UserPreferencesRepository
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.data.models.ProductSaleType
import com.example.e_commerce.shopping_feature.domain.models.toProductUIModel
import com.example.e_commerce.shopping_feature.domain.repository.CategoriesRepository
import com.example.e_commerce.shopping_feature.domain.repository.SalesAdRepository
import com.example.e_commerce.shopping_feature.presentation.models.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdRepository: SalesAdRepository,
    private val categoriesRepository: CategoriesRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val salesAdsState = salesAdRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    private val countryState = userPreferencesRepository.getUserCountry().stateIn(
        viewModelScope + IO,
        started = SharingStarted.Eagerly,
        initialValue = CountryData.getDefaultInstance()
    )

    val flashSaleState = getProductsSales(ProductSaleType.FLASH_SALE)

    val megaSaleState = getProductsSales(ProductSaleType.MEGA_SALE)

    val isEmptyFlashSale = flashSaleState.map { it.isEmpty() }.asLiveData()

    val isEmptyMegaSale = megaSaleState.map { it.isEmpty() }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getProductsSales(productSaleType: ProductSaleType): StateFlow<List<ProductUIModel>> =
        countryState.filterNotNull().mapLatest { countryData ->
            Log.d(TAG, "Countryid for flah sale: ${countryData.id}")
            categoriesRepository.getSaleProducts(countryData.id ?: "0", productSaleType.type, 10)
        }.mapLatest { products ->
            products.firstOrNull()?.map { getProductModel(it) } ?: emptyList()
        }.stateIn(
            viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = emptyList()
        )


    private fun getProductModel(product: ProductModel): ProductUIModel {
        val productUIModel = product.toProductUIModel().copy(
            currencySymbol = countryState.value?.currencySymbol ?: ""
        )
        return productUIModel
    }

    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }

    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }

    }
}