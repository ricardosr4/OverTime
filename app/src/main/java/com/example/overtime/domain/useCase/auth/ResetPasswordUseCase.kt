package com.example.overtime.domain.useCase.auth

import com.example.overtime.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.resetPassword(email)
    }
} 