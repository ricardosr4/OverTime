package com.example.overtime.core.pdf

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.*
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

object MonthlyPdfScheduler {
    private const val UNIQUE_WORK_TEST = "monthly_pdf_work_test"
    private const val UNIQUE_WORK_MONTHLY = "monthly_pdf_work_monthly"

    /**
     * Función de PRUEBA: Descarga PDF cada 1 minuto
     * Esta función será eliminada después de las pruebas
     * NOTA: WorkManager requiere un mínimo de 15 minutos para trabajos periódicos
     * Para pruebas, usaremos un trabajo único que se reprograma cada minuto
     */
    fun scheduleTestPdfDownload(context: Context) {
        // WorkManager no permite trabajos periódicos menores a 15 minutos
        // Usamos un trabajo único que se reprograma cada minuto
        scheduleTestPdfDownloadOnce(context)
    }

    fun scheduleTestPdfDownloadOnce(context: Context) {
        try {
            // Verificar que WorkManager esté inicializado
            if (!WorkManager.isInitialized()) {
                Log.e("MonthlyPdfScheduler", "WorkManager no está inicializado")
                // Mostrar Toast en el hilo principal
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    Toast.makeText(context, "Error: WorkManager no inicializado", Toast.LENGTH_LONG).show()
                }
                return
            }
            
            // Programar para 1 minuto
            val request = OneTimeWorkRequestBuilder<MonthlyPdfWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag("test_pdf_download")
                .build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                UNIQUE_WORK_TEST,
                ExistingWorkPolicy.REPLACE,
                request
            )
            
            Log.d("MonthlyPdfScheduler", "Trabajo programado exitosamente")
            // Mostrar Toast en el hilo principal
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                Toast.makeText(context, "Trabajo programado para 1 minuto", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MonthlyPdfScheduler", "Error al programar trabajo: ${e.message}", e)
            // Mostrar Toast en el hilo principal
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                Toast.makeText(context, "Error al programar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Cancela la función de prueba
     */
    fun cancelTestPdfDownload(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_TEST)
    }

    /**
     * Función REAL: Descarga PDF el día de cierre de mes seleccionado
     */
    fun scheduleMonthlyPdfDownload(context: Context, closingDay: Int) {
        if (closingDay < 1 || closingDay > 31) {
            return
        }

        val delayMs = nextClosingDayDelayMs(closingDay)
        val request = OneTimeWorkRequestBuilder<MonthlyPdfWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
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
     */
    private fun nextClosingDayDelayMs(closingDay: Int): Long {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        
        // Obtener el día de cierre del mes actual
        val currentMonthClosing = now.withDayOfMonth(
            minOf(closingDay, now.toLocalDate().lengthOfMonth())
        ).withHour(0).withMinute(0).withSecond(0).withNano(0)

        // Si ya pasó el día de cierre de este mes, programar para el próximo mes
        val nextClosing = if (currentMonthClosing.isAfter(now)) {
            currentMonthClosing
        } else {
            // Próximo mes
            val nextMonth = now.plusMonths(1)
            nextMonth.withDayOfMonth(
                minOf(closingDay, nextMonth.toLocalDate().lengthOfMonth())
            ).withHour(0).withMinute(0).withSecond(0).withNano(0)
        }

        return Duration.between(now, nextClosing).toMillis()
    }
}

