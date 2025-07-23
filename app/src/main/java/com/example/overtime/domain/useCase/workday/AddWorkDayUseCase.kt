package com.example.overtime.domain.useCase.workday

import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.repository.WorkDayRepository
import javax.inject.Inject

class AddWorkDayUseCase @Inject constructor(
    private val repository: WorkDayRepository
) {
    suspend operator fun invoke(userId: String, workDay: WorkDay): Result<Unit> =
        repository.addWorkDay(userId, workDay)
} 