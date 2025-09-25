package com.example.overtime.data.repository

import com.example.overtime.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.overtime.data.model.UserModel
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario")
            val user = UserModel(userId = userId, email = email, userName = name)
            firestore.collection("Users").document(userId).set(user.toMap()).await()
            // Inicializar la lista de workdays vacía
            firestore.collection("Users").document(userId).update("workdays", emptyList<Map<String, Any>>()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                val userDoc = firestore.collection("Users").document(user.uid).get().await()
                if (!userDoc.exists()) {
                    val newUser = UserModel(
                        userName = user.displayName ?: "",
                        email = user.email ?: "",
                        userId = user.uid
                    )
                    firestore.collection("Users").document(user.uid).set(newUser).await()
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 