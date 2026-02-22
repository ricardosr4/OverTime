package com.example.overtime.core.pdf

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

/**
 * Se dispara por AlarmManager a la hora exacta del cierre de mes.
 * Enqueue el Worker inmediatamente (sin delay) para ejecutar la descarga del PDF.
 */
class MonthlyPdfAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<MonthlyPdfWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    companion object {
        const val WORK_NAME = "monthly_pdf_work_alarm"
    }
}
