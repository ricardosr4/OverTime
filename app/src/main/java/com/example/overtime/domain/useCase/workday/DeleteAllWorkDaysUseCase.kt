package com.example.overtime.domain.useCase.workday

import com.example.overtime.domain.repository.WorkDayRepository
import javax.inject.Inject

class DeleteAllWorkDaysUseCase @Inject constructor(
    private val repository: WorkDayRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> =
        repository.deleteAllWorkDays(userId)
} 