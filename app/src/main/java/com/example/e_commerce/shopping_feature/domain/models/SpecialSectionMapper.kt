package com.example.e_commerce.shopping_feature.domain.models

import com.example.e_commerce.shopping_feature.data.models.SpecialSectionModel
import com.example.e_commerce.shopping_feature.presentation.models.SpecialSectionUIModel

fun SpecialSectionModel.toSpecialSectionUIModel(): SpecialSectionUIModel {
    return SpecialSectionUIModel(
        id = id,
        title = title,
        description = description,
        type = type,
        image = image,
        enabled = enabled
    )
}

fun SpecialSectionUIModel.toSpecialSectionModel(): SpecialSectionModel {
    return SpecialSectionModel(
        id = id,
        title = title,
        description = description,
        type = type,
        image = image,
        enabled = enabled
    )
}