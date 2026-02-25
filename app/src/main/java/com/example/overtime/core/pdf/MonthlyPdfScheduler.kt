package com.example.overtime.core.pdf

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object MonthlyPdfScheduler {
    private const val TAG = "MonthlyPdfScheduler"
    private const val ALARM_REQUEST_CODE = 9001

    fun scheduleMonthlyPdfDownload(context: Context, closingDay: Int) {
        if (closingDay < 1 || closingDay > 31) return

        val triggerAtMillis = nextClosingDayEpochMillis(closingDay)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MonthlyPdfAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent
            )
        }

        val scheduled = ZonedDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(triggerAtMillis),
            ZoneId.systemDefault()
        )
        Log.i(TAG, "Alarma programada para: ${scheduled.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}")
    }

    fun cancelMonthlyPdfDownload(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MonthlyPdfAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Calcula el epoch en milisegundos del próximo cierre (día [closingDay] a las 00:00).
     * Si la fecha de este mes ya pasó, programa para el mes siguiente.
     */
    private fun nextClosingDayEpochMillis(closingDay: Int): Long {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)

        val currentMonthClosing = now.withDayOfMonth(
            minOf(closingDay, now.toLocalDate().lengthOfMonth())
        ).withHour(0).withMinute(0).withSecond(0).withNano(0)

        val nextClosing = if (currentMonthClosing.isAfter(now)) {
            currentMonthClosing
        } else {
            val nextMonth = now.plusMonths(1)
            nextMonth.withDayOfMonth(
                minOf(closingDay, nextMonth.toLocalDate().lengthOfMonth())
            ).withHour(0).withMinute(0).withSecond(0).withNano(0)
        }

        return nextClosing.toInstant().toEpochMilli()
    }
}
