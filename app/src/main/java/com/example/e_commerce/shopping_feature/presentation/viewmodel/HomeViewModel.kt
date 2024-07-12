package com.example.e_commerce.shopping_feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.domain.repository.CategoriesRepository
import com.example.e_commerce.shopping_feature.domain.repository.SalesAdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdRepository: SalesAdRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {
     val salesAdsState = salesAdRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }

    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }
    }
}