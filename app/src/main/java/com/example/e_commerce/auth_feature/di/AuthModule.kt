package com.example.e_commerce.auth_feature.di

import android.app.Application
import android.content.Context
import com.example.e_commerce.auth_feature.data.datastore.UserPreferencesDataSource
import com.example.e_commerce.auth_feature.data.repository.UserPreferenceRepository
import com.example.e_commerce.auth_feature.data.repository.UserPreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesUserPreferenceRepository(
        userPreferencesDataSource: UserPreferencesDataSource
    ): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(userPreferencesDataSource)
    }
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}