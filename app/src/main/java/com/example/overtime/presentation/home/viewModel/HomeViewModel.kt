package com.example.overtime.presentation.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _workDays = MutableStateFlow<List<WorkDay>>(emptyList())
    val workDays: StateFlow<List<WorkDay>> = _workDays

    private var workDaysListener: ListenerRegistration? = null

    init {
        loadWorkDaysFromFirebase()
    }

    private fun loadWorkDaysFromFirebase() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            workDaysListener?.remove()
            workDaysListener = Firebase.firestore
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val workDaysList = snapshot.documents.mapNotNull { document ->
                            val workDay = document.toObject(WorkDay::class.java)
                            workDay?.copy(id = document.id)
                        }
                        _workDays.value = workDaysList
                    }
                }
        }
    }

    fun deleteAllWorkDays() {
        viewModelScope.launch {
            try {
                val userId = Firebase.auth.currentUser?.uid
                if (userId != null) {
                    // Obtén todos los WorkDays desde Firebase
                    val workDaysSnapshot = Firebase.firestore
                        .collection("Users")
                        .document(userId)
                        .collection("workdays")
                        .get()
                        .await()

                    // Elimina todos los documentos en la colección
                    for (document in workDaysSnapshot.documents) {
                        document.reference.delete().await()
                    }

                    // Actualiza el estado para reflejar que no hay más WorkDays
                    _workDays.value = emptyList()  // Limpia la lista
                } else {
                    Log.e("Firebase", "El usuario no está autenticado.")
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Error al eliminar todos los WorkDays", e)
            }
        }
    }

    fun deleteWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            try {
                val userId = Firebase.auth.currentUser?.uid
                val workDayId = workDay.id

                if (userId != null && !workDayId.isNullOrEmpty()) {
                    Firebase.firestore
                        .collection("Users")
                        .document(userId)
                        .collection("workdays")
                        .document(workDayId)
                        .delete()
                        .await()
                } else {
                    Log.e("Firebase", "El ID del WorkDay es nulo o vacío, no se puede eliminar.")
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Error al eliminar el día de trabajo", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        workDaysListener?.remove()
    }
}
