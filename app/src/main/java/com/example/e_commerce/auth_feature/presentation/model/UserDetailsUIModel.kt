package com.example.e_commerce.auth_feature.presentation.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Keep
@Parcelize
data class UserDetailsUIModel(
    var createdAt: Int? = null,
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var reviews: List<String>? = null,
) : Parcelable {
    val formattedCreatedAt: String
        get() = createdAt?.let {
            val date = Date(it.toLong())
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        } ?: ""
}