package com.example.e_commerce.shopping_feature.domain.models

import com.example.e_commerce.shopping_feature.data.models.CategoryModel
import com.example.e_commerce.shopping_feature.presentation.models.CategoryUIModel

fun CategoryModel.toUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id, name = name, icon = icon
    )
}