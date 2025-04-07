package com.example.overtime.ui.screen.home.viewModel

import android.util.Log
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
        // Cuando el usuario cambia o inicia sesión, recargar los datos
        loadWorkDaysFromFirebase()
    }

    // Función para cargar los workdays de Firestore del usuario autenticado
    private fun loadWorkDaysFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Si ya existe un listener anterior, eliminamos para evitar duplicados
            workDaysListener?.remove()

            // Escuchar cambios en la subcolección 'workdays' del usuario autenticado
            workDaysListener = FirebaseFirestore.getInstance()
                .collection("Users")        // Colección de Usuarios
                .document(userId)           // Documento del usuario autenticado
                .collection("workdays")     // Subcolección workdays del usuario
                .addSnapshotListener { snapshot, exception ->   // Escucha cambios en tiempo real
                    if (exception != null) {
                        // Manejo de errores si falla la carga
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        // Mapeamos los documentos a una lista de WorkDay
                        val workDaysList = snapshot.documents.mapNotNull { document ->
                            val workDay = document.toObject(WorkDay::class.java)
                            workDay?.copy(id = document.id)  // Asignamos el ID del documento a WorkDay
                        }
                        _workDays.value = workDaysList  // Actualizamos la lista de workdays
                    }
                }
        }
    }

    // Función para eliminar un WorkDay
    fun deleteWorkDay(workDay: WorkDay) {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val workDayId = workDay.id

                if (userId != null && !workDayId.isNullOrEmpty()) { // ✅ Verificamos que id no sea null ni vacío
                    FirebaseFirestore.getInstance()
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
        // Detener el listener cuando el ViewModel sea destruido
        workDaysListener?.remove()
    }
}
