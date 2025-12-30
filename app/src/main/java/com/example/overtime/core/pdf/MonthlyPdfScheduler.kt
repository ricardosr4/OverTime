package com.example.overtime.core.pdf

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.*
import java.util.concurrent.TimeUnit

object MonthlyPdfScheduler {
    private const val UNIQUE_WORK_MONTHLY = "monthly_pdf_work_monthly"

    /**
     * Descarga PDF el día de cierre de mes seleccionado
     */
    fun scheduleMonthlyPdfDownload(context: Context, closingDay: Int) {
        if (closingDay < 1 || closingDay > 31) {
            return
        }

        val delayMs = nextClosingDayDelayMs(closingDay)
        
        // Constraints mínimas para asegurar ejecución en background
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Necesario para Firebase
            .build()
        
        val request = OneTimeWorkRequestBuilder<MonthlyPdfWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                UNIQUE_WORK_MONTHLY,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    /**
     * Cancela la descarga mensual programada
     */
    fun cancelMonthlyPdfDownload(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_MONTHLY)
    }

    /**
     * Calcula el delay hasta el próximo día de cierre de mes
     * 
     * NOTA: Para cambiar la hora de ejecución, modifica los valores en:
     * - .withHour(0) -> Cambiar por la hora deseada (0-23)
     * - .withMinute(0) -> Cambiar por el minuto deseado (0-59)
     * - .withSecond(0) -> Cambiar por el segundo deseado (0-59)
     * 
     * Ejemplo: Para ejecutar a las 23:30:00 (11:30 PM):
     * .withHour(23).withMinute(30).withSecond(0)
     */
    private fun nextClosingDayDelayMs(closingDay: Int): Long {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        
        // Obtener el día de cierre del mes actual
        val currentMonthClosing = now.withDayOfMonth(
            minOf(closingDay, now.toLocalDate().lengthOfMonth())
        ).withHour(1).withMinute(38).withSecond(0).withNano(0)

        // Si ya pasó el día de cierre de este mes, programar para el próximo mes
        val nextClosing = if (currentMonthClosing.isAfter(now)) {
            currentMonthClosing
        } else {
            // Próximo mes
            val nextMonth = now.plusMonths(1)
            nextMonth.withDayOfMonth(
                minOf(closingDay, nextMonth.toLocalDate().lengthOfMonth())
            ).withHour(1).withMinute(38).withSecond(0).withNano(0)
        }

        return Duration.between(now, nextClosing).toMillis()
    }
}

