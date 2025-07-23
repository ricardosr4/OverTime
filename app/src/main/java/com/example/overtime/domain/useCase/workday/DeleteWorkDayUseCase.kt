package com.example.overtime.domain.useCase.workday

import com.example.overtime.domain.repository.WorkDayRepository
import javax.inject.Inject

class DeleteWorkDayUseCase @Inject constructor(
    private val repository: WorkDayRepository
) {
    suspend operator fun invoke(userId: String, workDayId: String): Result<Unit> =
        repository.deleteWorkDay(userId, workDayId)
} 