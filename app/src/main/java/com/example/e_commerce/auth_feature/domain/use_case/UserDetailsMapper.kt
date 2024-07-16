package com.example.e_commerce.auth_feature.domain.use_case

import com.example.e_commerce.auth_feature.data.models.UserDetailsModel
import com.example.e_commerce.auth_feature.data.user.CountryData
import com.example.e_commerce.auth_feature.data.user.UserDetailsPreferences

fun UserDetailsPreferences.toUserDetailsModel(): UserDetailsModel {
    return UserDetailsModel(
        id = id,
        email = email,
        name = name,
        reviews = reviewsList
    )
}

fun UserDetailsModel.toUserDetailsPreferences(countryData: CountryData): UserDetailsPreferences {
    return UserDetailsPreferences.newBuilder()
        .setId(id)
        .setEmail(email)
        .setName(name)
        .addAllReviews(reviews?.toList() ?: emptyList())
        .setCountry(countryData)
        .build()
}