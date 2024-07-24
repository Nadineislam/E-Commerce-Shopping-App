package com.example.e_commerce.shopping_feature.domain.models

import com.example.e_commerce.shopping_feature.data.models.ProductColorModel
import com.example.e_commerce.shopping_feature.data.models.ProductModel
import com.example.e_commerce.shopping_feature.data.models.ProductSizeModel
import com.example.e_commerce.shopping_feature.presentation.models.ProductColorUIModel
import com.example.e_commerce.shopping_feature.presentation.models.ProductUIModel

fun ProductUIModel.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        name = name,
        description = description,
        categoriesIDs = categoriesIDs,
        images = images,
        price = price,
        rate = rate,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors.map {
            ProductColorModel(
                size = it.size,
                stock = it.stock,
                color = it.color
            )
        },
        sizes = sizes.map {
            ProductSizeModel(
                size = it.size,
                stock = it.stock
            )
        }
    )
}

fun ProductModel.toProductUIModel(): ProductUIModel {
    return ProductUIModel(
        id = id ?: throw IllegalArgumentException("Product ID is missing"),
        name = name ?: "No Name",
        description = description ?: "No Description",
        categoriesIDs = categoriesIDs ?: emptyList(),
        images = images ?: emptyList(),
        price = price ?: 0,
        rate = rate ?: 0f,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors?.map {
            ProductColorUIModel(
                size = it.size,
                stock = it.stock,
                color = it.color
            )
        } ?: emptyList(),
        sizes = sizes?.map {
            ProductSizeModel(
                size = it.size,
                stock = it.stock
            )
        } ?: emptyList()
    )
}