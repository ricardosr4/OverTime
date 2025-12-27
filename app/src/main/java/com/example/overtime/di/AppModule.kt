package com.example.overtime.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.data.repository.AuthRepositoryImpl
import com.example.overtime.data.repository.WorkDayRepositoryImpl
import com.example.overtime.domain.repository.AuthRepository
import com.example.overtime.domain.repository.WorkDayRepository
import com.example.overtime.domain.useCase.auth.LoginWithGoogleUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {
    // Hilt proporciona HiltWorkerFactory automáticamente, pero necesitamos exponerlo
    // No podemos usar @Binds aquí porque HiltWorkerFactory no es una interfaz
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideWorkDayRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): WorkDayRepository = WorkDayRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideLoginWithGoogleUseCase(repository: AuthRepository): LoginWithGoogleUseCase =
        LoginWithGoogleUseCase(repository)

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager =
        PreferencesManager(context)
    
    // HiltWorkerFactory es proporcionado automáticamente por hilt-work
    // No necesitamos proporcionarlo manualmente, Hilt lo hace internamente
}