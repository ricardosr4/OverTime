package com.example.overtime.core.pdf

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.overtime.R
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.domain.useCase.workday.DownloadWorkDaysPdfUseCase
import com.example.overtime.domain.useCase.workday.GetWorkDaysUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@HiltWorker
class MonthlyPdfWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val getWorkDaysUseCase: GetWorkDaysUseCase,
    private val downloadWorkDaysPdfUseCase: DownloadWorkDaysPdfUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preferencesManager: PreferencesManager
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d("MonthlyPdfWorker", "doWork() llamado")
        // Toast de inicio para verificar que el Worker se ejecuta
        showToast("Worker iniciado - Descargando PDF...")
        
        return try {
            // Verificar si es un trabajo de prueba (tiene el tag)
            val isTestWork = tags.contains("test_pdf_download")
            
            val userId = firebaseAuth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                showToast("Worker: Usuario no autenticado")
                return Result.retry()
            }

            // Obtener workDays primero (igual que en HomeViewModel)
            val workDays = getWorkDaysUseCase(userId).first()

            // Obtener nombre de usuario usando callbacks (igual que en HomeViewModel)
            val userName = suspendCancellableCoroutine<String> { continuation ->
                firestore.collection("Users").document(userId).get()
                    .addOnSuccessListener { document ->
                        val name = document.getString("userName") ?: "Usuario"
                        continuation.resume(name)
                    }
                    .addOnFailureListener { e ->
                        continuation.resume("Usuario") // Valor por defecto en caso de error
                    }
            }

            // Obtener logo como Bitmap (igual que en HomeViewModel)
            val logo = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.img_over_time
            )

            // Generar PDF (igual que en HomeViewModel)
            val result = downloadWorkDaysPdfUseCase(
                context = applicationContext,
                logo = logo,
                userName = userName,
                workDays = workDays
            )

            if (result.isSuccess) {
                // Mostrar Toast cuando se descarga el PDF
                val resultValue = result.getOrNull()
                val message = when (resultValue) {
                    is android.net.Uri -> "PDF descargado automáticamente correctamente"
                    is java.io.File -> "PDF descargado automáticamente: ${resultValue.name}"
                    else -> "PDF descargado automáticamente correctamente"
                }
                showToast(message)
                
                // Si es trabajo de prueba, reprogramar para el próximo minuto
                if (isTestWork) {
                    MonthlyPdfScheduler.scheduleTestPdfDownloadOnce(applicationContext)
                } else {
                    // Reprogramar para el próximo mes después de una descarga exitosa
                    val closingDay = preferencesManager.getMonthClosingDay()
                    if (closingDay > 0) {
                        MonthlyPdfScheduler.scheduleMonthlyPdfDownload(applicationContext, closingDay)
                    }
                }
                Result.success()
            } else {
                // Mostrar error en Toast
                val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Error desconocido al descargar PDF"
                showToast("Error: $errorMessage")
                Result.retry()
            }
        } catch (e: Exception) {
            showToast("Excepción en Worker: ${e.localizedMessage}")
            Result.retry()
        }
    }

    private fun showToast(message: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}

