package com.example.overtime.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.overtime.MainActivity
import com.example.overtime.R

object NotificationHelper {
    private const val CHANNEL_ID = "monthly_pdf_channel"
    private const val CHANNEL_NAME = "Cierre Mensual"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de cierre mensual y descarga de PDF"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showPdfDownloadedNotification(context: Context, monthName: String, totalHours: Int) {
        try {
            createNotificationChannel(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("PDF Descargado Exitosamente")
                .setContentText("$totalHours horas acumuladas del mes de $monthName")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Se descargó automáticamente el PDF con las horas extras acumuladas del mes de $monthName. Total: $totalHours horas.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (manager.areNotificationsEnabled()) {
                    manager.notify(NOTIFICATION_ID, notification)
                }
            } else {
                manager.notify(NOTIFICATION_ID, notification)
            }
        } catch (_: Exception) { }
    }
}
