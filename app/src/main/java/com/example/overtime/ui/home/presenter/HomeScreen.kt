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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.data.model.WorkDay
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val currentMonth = remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }
    val workDays by viewModel.workDays.collectAsState()

    // Calcular total de horas por porcentaje
    val total50 = workDays.filter { it.percentageOverHours == 50 }.sumOf { it.quantityOverHours }
    val total75 = workDays.filter { it.percentageOverHours == 75 }.sumOf { it.quantityOverHours }
    val total100 = workDays.filter { it.percentageOverHours == 100 }.sumOf { it.quantityOverHours }
    val total130 = workDays.filter { it.percentageOverHours == 130 }.sumOf { it.quantityOverHours }

    Column(modifier = Modifier.fillMaxSize()) {
        // Card superior con totales de horas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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

                // Mostrar el total de horas agrupadas por porcentaje
                Text(text = "$total130 hrs al 130%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total100 hrs al 100%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total75 hrs al 75%", fontSize = 16.sp, color = Color.Black)
                Text(text = "$total50 hrs al 50%", fontSize = 16.sp, color = Color.Black)
            }
        }

        Button(
            onClick = { navController.navigate("add_hrs_extras") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
        ) {
            Text("Agregar Horas Extras")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workDays) { item ->
                CardItem(
                    workDay = item,
                    onDeleteConfirm = { viewModel.deleteWorkDay(item) } // Llamar función de eliminación
                )
            }
        }
    }
}

@Composable
fun CardItem(
    workDay: WorkDay,
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
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = workDay.weekDay, fontSize = 16.sp)
                Text(text = "Horas extras: ${workDay.quantityOverHours}", fontSize = 14.sp)
                Text(text = "Porcentaje: ${workDay.percentageOverHours}%", fontSize = 14.sp)
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
