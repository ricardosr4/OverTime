package com.example.overtime

import android.app.Application
import com.example.overtime.core.notifications.NotificationChannels
import com.example.overtime.core.notifications.WeeklyOvertimeScheduler
import com.example.overtime.core.prefs.PreferencesManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OverTimeApplication: Application() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreate() {
        super.onCreate()
        NotificationChannels.createAll(this)
        if (prefs.getNotificationsEnabled()) {
            WeeklyOvertimeScheduler.scheduleNextMondayNoon(this)
        }
    }
}