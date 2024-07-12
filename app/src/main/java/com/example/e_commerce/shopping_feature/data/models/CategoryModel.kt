package com.example.e_commerce.shopping_feature.data.models

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class CategoryModel(
    val id: String? = null,
    val name: String? = null,
    val icon: String? = null,
) : Parcelable {
    override fun toString(): String {

        return "id = ${id}, name = $name"
    }
}
