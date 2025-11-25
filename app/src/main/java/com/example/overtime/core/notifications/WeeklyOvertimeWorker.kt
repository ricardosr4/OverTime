package com.example.overtime.core.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.overtime.domain.useCase.workday.GetWorkDaysUseCase
import com.example.overtime.data.model.WorkDay
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

@HiltWorker
class WeeklyOvertimeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val getWorkDaysUseCase: GetWorkDaysUseCase,
    private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val userId = firebaseAuth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                // No hay usuario → no hay datos que contar; reprogramar siguiente lunes y salir
                WeeklyOvertimeScheduler.scheduleNextMondayNoon(applicationContext)
                return Result.success()
            }

            val (start, end) = lastWeekRange()
            val workDays: List<WorkDay> = getWorkDaysUseCase(userId).first()

            val total = workDays
                .filter { it.isInRange(start, end) }
                .sumOf { it.quantityOverHours }

            WeeklyOvertimeNotifier.notify(applicationContext, total)
            WeeklyOvertimeScheduler.scheduleNextMondayNoon(applicationContext)
            Result.success()
        } catch (e: Exception) {
            WeeklyOvertimeScheduler.scheduleNextMondayNoon(applicationContext)
            Result.retry()
        }
    }

    private fun lastWeekRange(): Pair<ZonedDateTime, ZonedDateTime> {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        val thisMonday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val start = thisMonday.minusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val end = thisMonday.withHour(11).withMinute(59).withSecond(0).withNano(0)
        return start to end
    }

    // ADAPTA esta extensión a tu modelo real de fecha en WorkDay
    private fun WorkDay.isInRange(start: ZonedDateTime, end: ZonedDateTime): Boolean {
        // EJEMPLO 1 (si tienes string "yyyy-MM-dd"):
        // val localDate = java.time.LocalDate.parse(this.date, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
        // val zdt = localDate.atStartOfDay(ZoneId.systemDefault())
        // return !zdt.isBefore(start) && !zdt.isAfter(end)

        // EJEMPLO 2 (si tienes epochMillis):
        // val zdt = java.time.Instant.ofEpochMilli(this.timestamp).atZone(ZoneId.systemDefault())
        // return !zdt.isBefore(start) && !zdt.isAfter(end)

        return true // placeholder: ajusta según tu modelo.
    }
}