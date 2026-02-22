package com.example.overtime.core.pdf

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.overtime.core.prefs.PreferencesManager

/**
 * Re-programa la alarma de cierre mensual después de un reinicio del dispositivo,
 * ya que las alarmas de AlarmManager no sobreviven a reinicios.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val prefs = PreferencesManager(context)
        val closingDay = prefs.getMonthClosingDay()
        if (closingDay > 0) {
            MonthlyPdfScheduler.scheduleMonthlyPdfDownload(context, closingDay)
        }
    }
}
