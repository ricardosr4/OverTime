package com.example.overtime.domain.useCase.workday

import com.example.overtime.domain.repository.WorkDayRepository
import java.time.LocalDate
import javax.inject.Inject

class DeleteWorkDaysByDateRangeUseCase @Inject constructor(
    private val repository: WorkDayRepository
) {
    suspend operator fun invoke(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Unit> =
        repository.deleteWorkDaysByDateRange(userId, startDate, endDate)
}


