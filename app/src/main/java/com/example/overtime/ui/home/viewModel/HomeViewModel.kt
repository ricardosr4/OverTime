package com.example.overtime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overtime.data.model.WorkDay
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    private val _workDays = MutableStateFlow<List<WorkDay>>(emptyList())
    val workDays: StateFlow<List<WorkDay>> = _workDays

    private var workDaysListener: ListenerRegistration? = null

    init {
        // Cargar los datos desde Firebase y escuchar cambios en tiempo real
        loadWorkDaysFromFirebase()
    }

    private fun loadWorkDaysFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Escuchar los cambios en la subcolección 'workdays' para el usuario autenticado
            workDaysListener = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .collection("workdays")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        // Manejo de errores si falla la carga
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        // Mapear los documentos a la lista de WorkDay
                        val workDaysList = snapshot.documents.mapNotNull { document ->
                            val workDay = document.toObject(WorkDay::class.java)
                            workDay?.copy(id = document.id) // Asignar el ID del documento a WorkDay
                        }
                        _workDays.value = workDaysList
                    }
                }
        }
    }

    // Función para eliminar un WorkDay
    fun deleteWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userId)
                        .collection("workdays")
                        .document(workDay.id)  // Usamos el ID para eliminar el documento correcto
                        .delete()
                        .await()
                }
            } catch (e: Exception) {
                // Manejo de errores si falla la eliminación
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Detener el listener cuando el ViewModel sea destruido
        workDaysListener?.remove()
    }
}
