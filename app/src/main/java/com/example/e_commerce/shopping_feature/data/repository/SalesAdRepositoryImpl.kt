package com.example.e_commerce.shopping_feature.data.repository

import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.shopping_feature.data.models.SalesAdModel
import com.example.e_commerce.shopping_feature.domain.repository.SalesAdRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdRepositoryImpl @Inject constructor(private val fireStore:FirebaseFirestore):SalesAdRepository {
    override  fun getSalesAds() = flow {
        try {
            emit(Resource.Loading())
            val salesAds =
                fireStore.collection("sales_ads")
                    .get().await().toObjects(SalesAdModel::class.java)

            emit(Resource.Success(salesAds.map { it.toUIModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}