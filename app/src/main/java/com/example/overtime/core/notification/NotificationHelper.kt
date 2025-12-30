package com.example.overtime.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.overtime.MainActivity
import com.example.overtime.R

object NotificationHelper {
    private const val CHANNEL_ID = "monthly_pdf_channel"
    private const val CHANNEL_NAME = "Cierre Mensual"
    private const val NOTIFICATION_ID = 1001

    /**
     * Crea el canal de notificaciones (requerido para Android 8.0+)
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notificaciones de cierre mensual y descarga de PDF"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Muestra una notificación cuando se descarga el PDF automáticamente
     */
    fun showPdfDownloadedNotification(
        context: Context,
        monthName: String,
        totalHours: Int
    ) {
        Log.d("NotificationHelper", "Mostrando notificación: $totalHours horas en $monthName")
        
        try {
            createNotificationChannel(context)

            // Intent para abrir la app cuando se toque la notificación
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("PDF Descargado Exitosamente")
                .setContentText("Se descargó el PDF con $totalHours horas acumuladas del mes de $monthName")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Se descargó automáticamente el PDF con las horas extras acumuladas del mes de $monthName. Total: $totalHours horas.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)
                    ?: context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Verificar permiso de notificaciones (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(NOTIFICATION_ID, notification)
                    Log.d("NotificationHelper", "Notificación mostrada exitosamente")
                } else {
                    Log.w("NotificationHelper", "Permisos de notificación no otorgados")
                }
            } else {
                notificationManager.notify(NOTIFICATION_ID, notification)
                Log.d("NotificationHelper", "Notificación mostrada exitosamente (Android < 13)")
            }
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error al mostrar notificación: ${e.message}", e)
        }
    }
}

