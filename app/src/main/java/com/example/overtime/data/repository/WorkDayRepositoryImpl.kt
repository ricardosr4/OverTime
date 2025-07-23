package com.example.overtime.data.repository

import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.repository.WorkDayRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WorkDayRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : WorkDayRepository {
    override fun getWorkDays(userId: String): Flow<List<WorkDay>> = callbackFlow {
        val listenerRegistration: ListenerRegistration = firestore
            .collection("Users")
            .document(userId)
            .collection("workdays")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val workDaysList = snapshot.documents.mapNotNull { document ->
                        val workDay = document.toObject(WorkDay::class.java)
                        workDay?.copy(id = document.id)
                    }
                    trySend(workDaysList)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addWorkDay(userId: String, workDay: WorkDay): Result<Unit> {
        return try {
            val docRef = firestore
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .add(workDay)
                .await()
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkDay(userId: String, workDayId: String): Result<Unit> {
        return try {
            firestore
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .document(workDayId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAllWorkDays(userId: String): Result<Unit> {
        return try {
            val workDaysSnapshot = firestore
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .get()
                .await()
            for (document in workDaysSnapshot.documents) {
                document.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 