package com.example.overtime.core.prefs

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(PrefsKeys.PREFS_NAME, Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(readThemeMode())
    val themeModeFlow: StateFlow<ThemeMode> = _themeMode

    private val _monthClosingDay = MutableStateFlow(prefs.getInt(PrefsKeys.KEY_MONTH_CLOSING_DAY, 0))
    val monthClosingDayFlow: StateFlow<Int> = _monthClosingDay

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean(PrefsKeys.KEY_NOTIFICATIONS_ENABLED, true))
    val notificationsEnabledFlow: StateFlow<Boolean> = _notificationsEnabled

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PrefsKeys.KEY_THEME_MODE -> _themeMode.value = readThemeMode()
            PrefsKeys.KEY_MONTH_CLOSING_DAY -> _monthClosingDay.value = prefs.getInt(PrefsKeys.KEY_MONTH_CLOSING_DAY, 0)
            PrefsKeys.KEY_NOTIFICATIONS_ENABLED -> _notificationsEnabled.value = prefs.getBoolean(PrefsKeys.KEY_NOTIFICATIONS_ENABLED, true)
        }
    }

    init { prefs.registerOnSharedPreferenceChangeListener(listener) }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(PrefsKeys.KEY_THEME_MODE, mode.name).apply()
    }
    fun getThemeMode(): ThemeMode = _themeMode.value

    fun setMonthClosingDay(day: Int) {
        prefs.edit().putInt(PrefsKeys.KEY_MONTH_CLOSING_DAY, day).apply()
    }
    fun getMonthClosingDay(): Int = _monthClosingDay.value

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PrefsKeys.KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }
    fun areNotificationsEnabled(): Boolean = _notificationsEnabled.value

    private fun readThemeMode(): ThemeMode = when (prefs.getString(PrefsKeys.KEY_THEME_MODE, ThemeMode.SYSTEM.name)) {
        ThemeMode.LIGHT.name -> ThemeMode.LIGHT
        ThemeMode.DARK.name -> ThemeMode.DARK
        else -> ThemeMode.SYSTEM
    }
}