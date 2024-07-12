package com.example.e_commerce.shopping_feature.data.models

import android.os.Parcelable
import com.example.e_commerce.shopping_feature.presentation.models.SalesAdUIModel
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Keep
@Parcelize
data class SalesAdModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String? = null,
    val type: String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId: String? = null,

    @get:PropertyName("category_id")
    @set:PropertyName("category_id")
    var categoryId: String? = null,

    @get:PropertyName("external_link")
    @set:PropertyName("external_link")
    var externalLink: String? = null,

    @ServerTimestamp
    @get:PropertyName("end_at")
    @set:PropertyName("end_at")
    var endAt: Date? = null
): Parcelable {
    fun toUIModel(): SalesAdUIModel {
        return SalesAdUIModel(
            id = this.id,
            title = this.title,
            description = this.description,
            imageUrl = this.imageUrl,
            type = this.type,
            productId = this.productId,
            categoryId = this.categoryId,
            externalLink = this.externalLink,
            endAt = this.endAt
        )
    }
}