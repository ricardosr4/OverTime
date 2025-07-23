package com.example.overtime.domain.useCase.workday

import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.repository.WorkDayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkDaysUseCase @Inject constructor(
    private val repository: WorkDayRepository
) {
    operator fun invoke(userId: String): Flow<List<WorkDay>> = repository.getWorkDays(userId)
} 