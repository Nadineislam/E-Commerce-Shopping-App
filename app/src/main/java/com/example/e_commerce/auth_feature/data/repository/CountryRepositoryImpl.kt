package com.example.e_commerce.auth_feature.data.repository

import com.example.e_commerce.auth_feature.data.models.CountryModel
import com.example.e_commerce.auth_feature.domain.repository.CountryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(val fireStore: FirebaseFirestore) :
    CountryRepository {
    override fun getCountries(): Flow<List<CountryModel>> = flow {
        val countries = fireStore.collection("countries").get().await().toObjects(CountryModel::class.java)
        emit(countries)
    }
}