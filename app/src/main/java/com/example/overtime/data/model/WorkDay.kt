package com.example.overtime.data.model


data class WorkDay(
    val id: String? = null, // El ID es opcional al crear el objeto, lo asignamos cuando cargamos los datos de Firebase
    val weekDay: String = "", // Campo de fecha con valor predeterminado
    val quantityOverHours: Int = 0, // Horas extras con valor predeterminado
    val percentageOverHours: Int = 50 // Porcentaje de horas extras con valor predeterminado
)