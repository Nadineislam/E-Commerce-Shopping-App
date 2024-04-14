package com.example.e_commerce.auth_feature.data.models

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.android.parcel.Parcelize
import com.google.firebase.firestore.PropertyName

@Keep
@Parcelize
data class UserDetailsModel(
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Long? = null,
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var disabled: Boolean? = null,
    var reviews: List<String>? = null,
):Parcelable