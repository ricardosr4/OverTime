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
import java.time.LocalDate
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
        val closingDay = preferencesManager.getMonthClosingDay()

        return try {
            val userId = firebaseAuth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                rescheduleIfNeeded(closingDay)
                return Result.failure()
            }

            if (closingDay <= 0) return Result.success()

            val (startDate, endDate) = calculateClosingPeriod(closingDay)
            val allWorkDays = getWorkDaysUseCase(userId).first()
            val workDaysInPeriod = filterWorkDaysByPeriod(allWorkDays, startDate, endDate)

            if (workDaysInPeriod.isEmpty()) {
                rescheduleIfNeeded(closingDay)
                return Result.success()
            }

            val userName = suspendCancellableCoroutine<String> { continuation ->
                firestore.collection("Users").document(userId).get()
                    .addOnSuccessListener { doc ->
                        continuation.resume(doc.getString("userName") ?: "Usuario")
                    }
                    .addOnFailureListener { continuation.resume("Usuario") }
            }

            val logo = try {
                BitmapFactory.decodeResource(applicationContext.resources, R.drawable.img_over_time)
            } catch (_: Exception) { null }

            val result = downloadWorkDaysPdfUseCase(
                context = applicationContext,
                logo = logo,
                userName = userName,
                workDays = workDaysInPeriod
            )

            if (result.isSuccess) {
                deleteWorkDaysByDateRangeUseCase(userId, startDate, endDate)

                if (preferencesManager.areNotificationsEnabled()) {
                    val totalHours = workDaysInPeriod.sumOf { it.quantityOverHours }
                    val monthName = endDate.month.getDisplayName(
                        TextStyle.FULL, Locale.getDefault()
                    ).replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    NotificationHelper.showPdfDownloadedNotification(
                        context = applicationContext,
                        monthName = monthName,
                        totalHours = totalHours
                    )
                }

                rescheduleIfNeeded(closingDay)
                Result.success()
            } else {
                rescheduleIfNeeded(closingDay)
                Result.failure()
            }
        } catch (_: Exception) {
            rescheduleIfNeeded(closingDay)
            Result.failure()
        }
    }

    private fun rescheduleIfNeeded(closingDay: Int) {
        if (closingDay > 0) {
            MonthlyPdfScheduler.scheduleMonthlyPdfDownload(applicationContext, closingDay)
        }
    }

    private fun parseDateFromWeekDay(weekDay: String): LocalDate? {
        if (weekDay.isBlank()) return null
        return try {
            LocalDate.parse(weekDay, dateFormatter)
        } catch (_: Exception) {
            try {
                val datePart = weekDay.substringAfter(" ").trim()
                LocalDate.parse(datePart, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()))
            } catch (_: Exception) { null }
        }
    }

    /**
     * Calcula el período de cierre: desde el día después del cierre anterior hasta el cierre actual.
     * Ejemplo con cierre día 15 y hoy 15 de febrero: startDate=16/01, endDate=15/02
     */
    private fun calculateClosingPeriod(closingDay: Int): Pair<LocalDate, LocalDate> {
        val now = LocalDate.now()
        val actualClosingDay = minOf(closingDay, now.lengthOfMonth())
        val endDate = now.withDayOfMonth(actualClosingDay)

        val previousMonth = now.minusMonths(1)
        val previousClosingDay = minOf(closingDay, previousMonth.lengthOfMonth())
        val startDate = previousMonth.withDayOfMonth(previousClosingDay).plusDays(1)

        return Pair(startDate, endDate)
    }

    private fun filterWorkDaysByPeriod(
        workDays: List<WorkDay>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WorkDay> {
        return workDays.filter { workDay ->
            val date = parseDateFromWeekDay(workDay.weekDay) ?: return@filter false
            !date.isBefore(startDate) && !date.isAfter(endDate)
        }
    }
}
