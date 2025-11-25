package com.example.overtime.core.prefs

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(PrefsKeys.PREFS_NAME, Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(readThemeMode())
    val themeModeFlow: StateFlow<ThemeMode> = _themeMode

    private val _notifEnabled = MutableStateFlow(prefs.getBoolean(PrefsKeys.KEY_NOTIF_ENABLED, false))
    val notificationsEnabledFlow: StateFlow<Boolean> = _notifEnabled

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PrefsKeys.KEY_THEME_MODE -> _themeMode.value = readThemeMode()
            PrefsKeys.KEY_NOTIF_ENABLED -> _notifEnabled.value = prefs.getBoolean(PrefsKeys.KEY_NOTIF_ENABLED, false)
        }
    }

    init { prefs.registerOnSharedPreferenceChangeListener(listener) }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(PrefsKeys.KEY_THEME_MODE, mode.name).apply()
    }
    fun getThemeMode(): ThemeMode = _themeMode.value

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PrefsKeys.KEY_NOTIF_ENABLED, enabled).apply()
    }
    fun getNotificationsEnabled(): Boolean = _notifEnabled.value

    private fun readThemeMode(): ThemeMode = when (prefs.getString(PrefsKeys.KEY_THEME_MODE, ThemeMode.SYSTEM.name)) {
        ThemeMode.LIGHT.name -> ThemeMode.LIGHT
        ThemeMode.DARK.name -> ThemeMode.DARK
        else -> ThemeMode.SYSTEM
    }
}