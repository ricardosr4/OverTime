package com.example.overtime.core.pdf

import android.content.Context
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.overtime.R
import com.example.overtime.core.notification.NotificationHelper
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.useCase.workday.DeleteWorkDaysByDateRangeUseCase
import com.example.overtime.domain.useCase.workday.DownloadWorkDaysPdfUseCase
import com.example.overtime.domain.useCase.workday.GetWorkDaysUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.util.Log
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@HiltWorker
class MonthlyPdfWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val getWorkDaysUseCase: GetWorkDaysUseCase,
    private val downloadWorkDaysPdfUseCase: DownloadWorkDaysPdfUseCase,
    private val deleteWorkDaysByDateRangeUseCase: DeleteWorkDaysByDateRangeUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preferencesManager: PreferencesManager
) : CoroutineWorker(appContext, params) {

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale("es", "ES"))

    override suspend fun doWork(): Result {
        Log.d("MonthlyPdfWorker", "doWork() iniciado")
        return try {
            val userId = firebaseAuth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                Log.e("MonthlyPdfWorker", "Usuario no autenticado")
                return Result.retry()
            }

            val closingDay = preferencesManager.getMonthClosingDay()
            if (closingDay <= 0) {
                Log.d("MonthlyPdfWorker", "No hay día de cierre configurado")
                return Result.success()
            }
            
            val actualClosingDay = closingDay
            
            Log.d("MonthlyPdfWorker", "Día de cierre: $actualClosingDay")

            // Calcular el período de cierre
            val (startDate, endDate) = calculateClosingPeriod(actualClosingDay)
            Log.d("MonthlyPdfWorker", "Período calculado: $startDate a $endDate")

            // Obtener todos los WorkDays
            val allWorkDays = getWorkDaysUseCase(userId).first()
            Log.d("MonthlyPdfWorker", "Total WorkDays obtenidos: ${allWorkDays.size}")

            // Filtrar WorkDays del período
            val workDaysInPeriod = filterWorkDaysByPeriod(allWorkDays, startDate, endDate)
            Log.d("MonthlyPdfWorker", "WorkDays en período: ${workDaysInPeriod.size}")

            // Si no hay WorkDays en el período, no hacer nada
            if (workDaysInPeriod.isEmpty()) {
                Log.d("MonthlyPdfWorker", "No hay WorkDays en el período, finalizando")
                // Reprogramar para el próximo mes
                MonthlyPdfScheduler.scheduleMonthlyPdfDownload(applicationContext, closingDay)
                return Result.success()
            }

            // Obtener nombre de usuario
            val userName = suspendCancellableCoroutine<String> { continuation ->
                firestore.collection("Users").document(userId).get()
                    .addOnSuccessListener { document ->
                        val name = document.getString("userName") ?: "Usuario"
                        continuation.resume(name)
                    }
                    .addOnFailureListener { e ->
                        continuation.resume("Usuario")
                    }
            }

            // Obtener logo
            val logo = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.img_over_time
            )

            // Generar PDF solo con WorkDays del período
            Log.d("MonthlyPdfWorker", "Generando PDF con ${workDaysInPeriod.size} WorkDays")
            val result = downloadWorkDaysPdfUseCase(
                context = applicationContext,
                logo = logo,
                userName = userName,
                workDays = workDaysInPeriod
            )

            if (result.isSuccess) {
                Log.d("MonthlyPdfWorker", "PDF generado exitosamente")
                // Eliminar WorkDays del período después de descarga exitosa
                deleteWorkDaysByDateRangeUseCase(userId, startDate, endDate)

                // Calcular total de horas acumuladas
                val totalHours = workDaysInPeriod.sumOf { it.quantityOverHours }
                Log.d("MonthlyPdfWorker", "Total horas acumuladas: $totalHours")

                // Obtener nombre del mes actual
                val monthName = endDate.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                ).replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                }
                Log.d("MonthlyPdfWorker", "Mes: $monthName")

                // Mostrar notificación
                Log.d("MonthlyPdfWorker", "Llamando a NotificationHelper")
                NotificationHelper.showPdfDownloadedNotification(
                    context = applicationContext,
                    monthName = monthName,
                    totalHours = totalHours
                )

                // Reprogramar para el próximo mes
                MonthlyPdfScheduler.scheduleMonthlyPdfDownload(applicationContext, closingDay)
                Log.d("MonthlyPdfWorker", "doWork() completado exitosamente")
                Result.success()
            } else {
                Log.e("MonthlyPdfWorker", "Error al generar PDF: ${result.exceptionOrNull()?.message}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("MonthlyPdfWorker", "Excepción en doWork(): ${e.message}", e)
            Result.retry()
        }
    }

    /**
     * Parsea el String weekDay (formato "EEEE dd/MM/yyyy") a LocalDate
     */
    private fun parseDateFromWeekDay(weekDay: String): LocalDate? {
        if (weekDay.isBlank()) {
            return null
        }
        return try {
            val parsed = LocalDate.parse(weekDay, dateFormatter)
            parsed
        } catch (e: Exception) {
            Log.e("MonthlyPdfWorker", "Error al parsear fecha '$weekDay': ${e.message}")
            // Intentar parsear solo la parte de fecha si falla (dd/MM/yyyy)
            try {
                val datePart = weekDay.substringAfter(" ").trim()
                val simpleFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                LocalDate.parse(datePart, simpleFormatter)
            } catch (e2: Exception) {
                Log.e("MonthlyPdfWorker", "Error al parsear fecha simple: ${e2.message}")
                null
            }
        }
    }

    /**
     * Calcula el período de cierre mensual
     * Retorna un Pair con (startDate, endDate)
     * startDate: día después del último cierre
     * endDate: día de cierre actual (hoy, cuando se ejecuta el Worker)
     * 
     * Ejemplo: Si el día de cierre es 15 y hoy es 15 de febrero:
     * - endDate: 15 de febrero
     * - startDate: 16 de enero (día después del cierre de enero)
     */
    private fun calculateClosingPeriod(closingDay: Int): Pair<LocalDate, LocalDate> {
        val now = LocalDate.now()

        // El día de cierre es hoy (cuando se ejecuta el Worker)
        // Si el mes tiene menos días que el día de cierre, usar el último día del mes
        val lastDayOfMonth = now.lengthOfMonth()
        val actualClosingDay = minOf(closingDay, lastDayOfMonth)
        val endDate = now.withDayOfMonth(actualClosingDay)

        // Calcular el día de inicio: día después del cierre del mes anterior
        val previousMonth = now.minusMonths(1)
        val previousMonthLastDay = previousMonth.lengthOfMonth()
        val previousMonthClosingDay = minOf(closingDay, previousMonthLastDay)
        val previousMonthClosingDate = previousMonth.withDayOfMonth(previousMonthClosingDay)
        val startDate = previousMonthClosingDate.plusDays(1)

        return Pair(startDate, endDate)
    }

    /**
     * Filtra WorkDays que estén dentro del rango de fechas (inclusive)
     */
    private fun filterWorkDaysByPeriod(
        workDays: List<WorkDay>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WorkDay> {
        Log.d("MonthlyPdfWorker", "Filtrando WorkDays entre $startDate y $endDate")
        return workDays.filter { workDay ->
            Log.d("MonthlyPdfWorker", "WorkDay weekDay: '${workDay.weekDay}'")
            val workDayDate = parseDateFromWeekDay(workDay.weekDay)
            if (workDayDate == null) {
                Log.w("MonthlyPdfWorker", "No se pudo parsear la fecha: '${workDay.weekDay}'")
                false
            } else {
                val isInRange = !workDayDate.isBefore(startDate) && !workDayDate.isAfter(endDate)
                Log.d("MonthlyPdfWorker", "Fecha parseada: $workDayDate, en rango: $isInRange")
                isInRange
            }
        }
    }
}

