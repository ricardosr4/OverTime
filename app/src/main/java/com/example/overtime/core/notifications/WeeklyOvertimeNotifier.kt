package com.example.overtime.core.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.overtime.R

object WeeklyOvertimeNotifier {
    private const val NOTIF_ID = 2001

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notify(context: Context, totalHours: Int) {
        val text = "Esta semana hiciste $totalHours hrs extras"
        val notif = NotificationCompat.Builder(context, NotificationChannels.WEEKLY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // usa un recurso válido
            .setContentTitle("Resumen semanal")
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID, notif)
    }
}