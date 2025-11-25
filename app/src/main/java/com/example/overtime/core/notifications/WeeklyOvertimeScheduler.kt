package com.example.overtime.core.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.*
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

object WeeklyOvertimeScheduler {
    private const val UNIQUE_WORK = "weekly_overtime_work"

    fun scheduleNextMondayNoon(context: Context) {
        val delayMs = nextMondayNoonDelayMs()
        val req = OneTimeWorkRequestBuilder<WeeklyOvertimeWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(UNIQUE_WORK, ExistingWorkPolicy.REPLACE, req)
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK)
    }

    private fun nextMondayNoonDelayMs(): Long {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        var next = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
            .withHour(12).withMinute(0).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusWeeks(1)
        return Duration.between(now, next).toMillis()
    }
    //funcion de prueba
    fun scheduleInMinutes(context: Context, minutes: Long) {
        val req = OneTimeWorkRequestBuilder<WeeklyOvertimeWorker>()
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(UNIQUE_WORK, ExistingWorkPolicy.REPLACE, req)
    }
}