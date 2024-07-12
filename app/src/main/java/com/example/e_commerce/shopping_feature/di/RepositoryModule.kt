package com.example.e_commerce.shopping_feature.di

import com.example.e_commerce.shopping_feature.data.repository.CategoriesRepositoryImpl
import com.example.e_commerce.shopping_feature.data.repository.SalesAdRepositoryImpl
import com.example.e_commerce.shopping_feature.domain.repository.CategoriesRepository
import com.example.e_commerce.shopping_feature.domain.repository.SalesAdRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideCategoriesRepository(categoriesRepositoryImpl: CategoriesRepositoryImpl):CategoriesRepository

    @Binds
    @Singleton
    abstract fun provideSalesAdRepository(salesAdRepositoryImpl: SalesAdRepositoryImpl):SalesAdRepository
}