package com.example.e_commerce.auth_feature.data.models

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CountryModel(
    val id: String? = null,
    val name: String? = null,
    val code: String? = null,
    val image: String? = null,
    val currency: String? = null,

    @get:PropertyName("currency_symbol")
    @set:PropertyName("currency_symbol")
    var currencySymbol: String? = null
) : Parcelable
