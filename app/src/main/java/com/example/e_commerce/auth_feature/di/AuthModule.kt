package com.example.e_commerce.auth_feature.di

import android.app.Application
import android.content.Context
import com.example.e_commerce.auth_feature.data.datastore.AppPreferencesDataStore
import com.example.e_commerce.auth_feature.data.repository.AppDataStoreRepositoryImpl
import com.example.e_commerce.auth_feature.data.repository.CountryRepositoryImpl
import com.example.e_commerce.auth_feature.domain.repository.FirebaseAuthRepository
import com.example.e_commerce.auth_feature.data.repository.FirebaseAuthRepositoryImpl
import com.example.e_commerce.auth_feature.data.repository.UserFirestoreRepositoryImpl
import com.example.e_commerce.auth_feature.data.repository.UserPreferencesRepositoryImpl
import com.example.e_commerce.auth_feature.domain.repository.AppDataStoreRepository
import com.example.e_commerce.auth_feature.domain.repository.CountryRepository
import com.example.e_commerce.auth_feature.domain.repository.UserFirestoreRepository
import com.example.e_commerce.auth_feature.domain.repository.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesFirebaseAuthRepository(
        auth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(auth,fireStore)
    }
    @Provides
    @Singleton
    fun providesAppDataStoreRepository(appPreferencesDataStore: AppPreferencesDataStore):AppDataStoreRepository{
        return AppDataStoreRepositoryImpl(appPreferencesDataStore)
    }

    @Provides
    @Singleton
    fun providesUserFireStoreRepository(fireStore: FirebaseFirestore):UserFirestoreRepository{
        return UserFirestoreRepositoryImpl(fireStore)
    }
    @Provides
    @Singleton
    fun providesUserPreferencesRepository(context: Context):UserPreferencesRepository{
        return UserPreferencesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providesCountryRepository(fireStore: FirebaseFirestore):CountryRepository{
        return CountryRepositoryImpl(fireStore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore() = FirebaseFirestore.getInstance()
}