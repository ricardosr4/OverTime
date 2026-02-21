package com.example.overtime

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.overtime.core.notification.NotificationHelper
import com.example.overtime.core.pdf.MonthlyPdfScheduler
import com.example.overtime.core.prefs.PreferencesManager
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OverTimeApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
        scheduleMonthlyPdfIfConfigured()
    }

    private fun scheduleMonthlyPdfIfConfigured() {
        try {
            val prefs = EntryPointAccessors.fromApplication(
                this, PreferencesEntryPoint::class.java
            ).preferencesManager()

            val closingDay = prefs.getMonthClosingDay()
            if (closingDay > 0) {
                MonthlyPdfScheduler.scheduleMonthlyPdfDownload(this, closingDay)
            }
        } catch (_: Exception) { }
    }

    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
    interface PreferencesEntryPoint {
        fun preferencesManager(): PreferencesManager
    }
}
