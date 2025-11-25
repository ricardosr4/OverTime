package com.example.overtime.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val WEEKLY_CHANNEL_ID = "weekly_overtime"

    fun createAll(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(
                    WEEKLY_CHANNEL_ID,
                    "Resumen semanal",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Notifica horas extras de la semana anterior" }
            )
        }
    }
}