package com.example.overtime.data.repository

import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.repository.WorkDayRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    override suspend fun deleteWorkDaysByDateRange(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Unit> {
        return try {
            android.util.Log.d("WorkDayRepositoryImpl", "Eliminando WorkDays del período $startDate a $endDate")
            
            // Obtener todos los WorkDays del usuario
            val workDaysSnapshot = firestore
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .get()
                .await()

            val formatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale("es", "ES"))
            val simpleFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

            var deletedCount = 0
            var totalChecked = 0
            
            // Filtrar y eliminar WorkDays dentro del rango de fechas
            for (document in workDaysSnapshot.documents) {
                val workDay = document.toObject(WorkDay::class.java)
                if (workDay != null) {
                    totalChecked++
                    try {
                        val workDayDate = try {
                            LocalDate.parse(workDay.weekDay, formatter)
                        } catch (e: Exception) {
                            // Fallback: parsear solo la parte de fecha
                            val datePart = workDay.weekDay.substringAfter(" ").trim()
                            LocalDate.parse(datePart, simpleFormatter)
                        }
                        
                        // Verificar si la fecha está dentro del rango (inclusive)
                        if (!workDayDate.isBefore(startDate) && !workDayDate.isAfter(endDate)) {
                            document.reference.delete().await()
                            deletedCount++
                            android.util.Log.d("WorkDayRepositoryImpl", "Eliminado WorkDay: ${workDay.weekDay} (fecha: $workDayDate)")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("WorkDayRepositoryImpl", "Error al parsear fecha '${workDay.weekDay}': ${e.message}")
                        continue
                    }
                }
            }
            android.util.Log.d("WorkDayRepositoryImpl", "Eliminados $deletedCount de $totalChecked WorkDays del período")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("WorkDayRepositoryImpl", "Error al eliminar WorkDays: ${e.message}", e)
            Result.failure(e)
        }
    }
} 