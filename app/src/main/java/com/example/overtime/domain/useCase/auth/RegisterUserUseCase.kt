package com.example.overtime.domain.useCase.auth

import com.example.overtime.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return repository.register(name, email, password)
    }
} 