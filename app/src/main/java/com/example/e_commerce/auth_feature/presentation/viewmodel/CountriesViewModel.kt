package com.example.e_commerce.auth_feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.auth_feature.domain.repository.CountryRepository
import com.example.e_commerce.auth_feature.domain.repository.UserPreferencesRepository
import com.example.e_commerce.auth_feature.domain.use_case.toUIModel
import com.example.e_commerce.auth_feature.presentation.model.CountryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    countriesRepository: CountryRepository,
    private val userPreferenceRepository: UserPreferencesRepository,
) : ViewModel() {

    private val countriesState = countriesRepository.getCountries().stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val countriesUIModelState = countriesState.mapLatest { countries ->
        countries.map { country ->
            country.toUIModel()
        }
    }

    fun saveUserCountry(country: CountryUIModel) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserCountry(country)
        }
    }
}