package com.example.overtime.domain.repository

import com.example.overtime.data.model.WorkDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkDayRepository {
    fun getWorkDays(userId: String): Flow<List<WorkDay>>
    suspend fun addWorkDay(userId: String, workDay: WorkDay): Result<Unit>
    suspend fun deleteWorkDay(userId: String, workDayId: String): Result<Unit>
    suspend fun deleteAllWorkDays(userId: String): Result<Unit>
    suspend fun deleteWorkDaysByDateRange(userId: String, startDate: LocalDate, endDate: LocalDate): Result<Unit>
} 