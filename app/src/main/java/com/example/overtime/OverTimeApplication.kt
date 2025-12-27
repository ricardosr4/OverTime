package com.example.overtime

import android.app.Application
import com.example.overtime.core.prefs.PreferencesManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OverTimeApplication: Application() {
    @Inject lateinit var prefs: PreferencesManager
}
