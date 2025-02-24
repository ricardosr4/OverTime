package com.example.overtime.ui.home.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.CardColor
import com.example.overtime.ui.theme.SecondaryColor
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// Definir el data class WorkDay
data class WorkDay(
    val weekDay: String,
    val quantityOverHours: Int,
    val percentageOverHours: Int
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val currentMonth =
        remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }

    // Lista con los ítems como WorkDay
    var itemsList by remember {
        mutableStateOf(List(21) { index ->
            WorkDay("Día $index", (1..5).random(), listOf(50, 75, 100, 130).random())
        })
    }

    // Variables para llevar un control de las horas acumuladas por cada porcentaje
    var total50 by remember { mutableStateOf(0) }
    var total75 by remember { mutableStateOf(0) }
    var total100 by remember { mutableStateOf(0) }
    var total130 by remember { mutableStateOf(0) }

    // Función para recalcular las horas de cada porcentaje
    fun recalculateHours() {
        total50 = itemsList.filter { it.percentageOverHours == 50 }.sumOf { it.quantityOverHours }
        total75 = itemsList.filter { it.percentageOverHours == 75 }.sumOf { it.quantityOverHours }
        total100 = itemsList.filter { it.percentageOverHours == 100 }.sumOf { it.quantityOverHours }
        total130 = itemsList.filter { it.percentageOverHours == 130 }.sumOf { it.quantityOverHours }
    }

    // Recalcular las horas al inicio
    recalculateHours()

    Column(modifier = Modifier.fillMaxSize()) {
        // Card superior
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Horas extras - $currentMonth",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Mostrar las horas acumuladas por porcentaje
                Text(text = "$total130 hrs al 130%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total100 hrs al 100%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total75 hrs al 75%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total50 hrs al 50%", fontSize = 16.sp, color = Color.Black)
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Calcular Total $",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // LazyColumn con los elementos dinámicos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(itemsList) { item ->
                CardItem(
                    weekDay = item.weekDay,
                    quantityOverHours = item.quantityOverHours,
                    percentageOverHours = item.percentageOverHours,
                    onDeleteConfirm = {
                        itemsList = itemsList - item
                        recalculateHours() // Recalcular horas al eliminar un ítem
                    }
                )
            }
        }
    }
}

@Composable
fun CardItem(
    weekDay: String,
    quantityOverHours: Int,
    percentageOverHours: Int,
    onDeleteConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este ítem?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirm()
                    showDialog = false
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = weekDay,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Horas extras: $quantityOverHours",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Porcentaje: $percentageOverHours%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}
