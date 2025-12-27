package com.example.overtime

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.core.pdf.MonthlyPdfScheduler
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@HiltAndroidApp
class OverTimeApplication: Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: androidx.hilt.work.HiltWorkerFactory
    
    override val workManagerConfiguration: Configuration
        get() {
            // Este getter se llama cuando WorkManager necesita la configuración
            // Asegurarnos de que workerFactory esté inicializado
            if (!::workerFactory.isInitialized) {
                Log.e("OverTimeApplication", "ERROR: workerFactory no está inicializado cuando se necesita")
                // Forzar la inicialización de Hilt si es necesario
                // Esto no debería ser necesario, pero por si acaso
            } else {
                Log.d("OverTimeApplication", "Proporcionando configuración con HiltWorkerFactory")
            }
            
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        }
    
    override fun onCreate() {
        super.onCreate()
        
        // Programar descarga automática si hay un día de cierre configurado
        // Usar EntryPoint para acceder a PreferencesManager
        val prefs = EntryPointAccessors.fromApplication(
            this,
            PreferencesEntryPoint::class.java
        ).preferencesManager()
        
        val closingDay = prefs.getMonthClosingDay()
        if (closingDay > 0) {
            // Programar después de que WorkManager esté inicializado
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                MonthlyPdfScheduler.scheduleMonthlyPdfDownload(this, closingDay)
            }, 500)
        }
    }
    
    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
    interface PreferencesEntryPoint {
        fun preferencesManager(): PreferencesManager
    }
}
