package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.data.models.CountryModel
import com.example.e_commerce.auth_feature.presentation.model.CountryUIModel

fun CountryModel.toUIModel(): CountryUIModel {
    return CountryUIModel(
        id = id,
        name = name,
        code = code,
        currency = currency,
        image = image,
        currencySymbol = currencySymbol
    )
}