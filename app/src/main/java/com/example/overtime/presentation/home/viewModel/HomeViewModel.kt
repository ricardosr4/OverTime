package com.example.overtime.presentation.home.viewModel

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.R
import com.example.overtime.data.model.WorkDay
import com.example.overtime.domain.useCase.workday.DeleteAllWorkDaysUseCase
import com.example.overtime.domain.useCase.workday.DeleteWorkDayUseCase
import com.example.overtime.domain.useCase.workday.DownloadWorkDaysPdfUseCase
import com.example.overtime.domain.useCase.workday.GetWorkDaysUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWorkDaysUseCase: GetWorkDaysUseCase,
    private val deleteWorkDayUseCase: DeleteWorkDayUseCase,
    private val deleteAllWorkDaysUseCase: DeleteAllWorkDaysUseCase,
    private val downloadWorkDaysPdfUseCase: DownloadWorkDaysPdfUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val appContext: Application
) : ViewModel() {

    private val _workDays = MutableStateFlow<List<WorkDay>>(emptyList())
    val workDays: StateFlow<List<WorkDay>> = _workDays

    private var userId: String? = firebaseAuth.currentUser?.uid

    private val _pdfResult = MutableStateFlow<Result<Any>?>(null)
    val pdfResult = _pdfResult.asStateFlow()

    init {
        observeWorkDays()
    }

    private fun observeWorkDays() {
        userId?.let { uid ->
            viewModelScope.launch {
                getWorkDaysUseCase(uid).collectLatest { workDaysList ->
                    _workDays.value = workDaysList
                }
            }
        }
    }

    fun deleteAllWorkDays() {
        userId?.let { uid ->
            viewModelScope.launch {
                val result = deleteAllWorkDaysUseCase(uid)
                if (result.isSuccess) {
                    _workDays.value = emptyList()
                } else {
                    Log.e("WorkDay", "Error al eliminar todos los WorkDays: ${result.exceptionOrNull()?.message}")
                }
            }
        }
    }

    fun deleteWorkDay(workDay: WorkDay) {
        val workDayId = workDay.id
        userId?.let { uid ->
            if (!workDayId.isNullOrEmpty()) {
                viewModelScope.launch {
                    val result = deleteWorkDayUseCase(uid, workDayId)
                    if (result.isFailure) {
                        Log.e("WorkDay", "Error al eliminar el día de trabajo: ${result.exceptionOrNull()?.message}")
                    }
                }
            } else {
                Log.e("WorkDay", "El ID del WorkDay es nulo o vacío, no se puede eliminar.")
            }
        }
    }

    fun downloadWorkDaysPdf() {
        val uid = userId ?: return
        val workDaysList = workDays.value
        viewModelScope.launch {
            // Obtener nombre de usuario en tiempo real
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    val userName = document.getString("userName") ?: "Usuario"
                    // Obtener logo como Bitmap
                    val logo = BitmapFactory.decodeResource(appContext.resources, R.drawable.img_over_time)
                    // Generar PDF
                    val result = downloadWorkDaysPdfUseCase(
                        context = appContext,
                        logo = logo,
                        userName = userName,
                        workDays = workDaysList
                    )
                    _pdfResult.value = result
                }
                .addOnFailureListener { e ->
                    _pdfResult.value = Result.failure(e)
                }
        }
    }

    fun clearPdfResult() {
        _pdfResult.value = null
    }
}
