package com.example.e_commerce.auth_feature.di

import android.app.Application
import android.content.Context
import com.example.e_commerce.auth_feature.data.datastore.UserPreferencesDataSource
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.auth_feature.data.repository.FirebaseAuthRepositoryImpl
import com.example.e_commerce.auth_feature.data.repository.UserPreferenceRepository
import com.example.e_commerce.auth_feature.data.repository.UserPreferenceRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
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

    @Provides
    @Singleton
    fun providesFirebaseAuthRepository(
        auth: FirebaseAuth
    ): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(auth)
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}