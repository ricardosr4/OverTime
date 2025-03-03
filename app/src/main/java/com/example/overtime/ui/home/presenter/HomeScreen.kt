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
import com.example.overtime.ui.viewmodel.OvertimeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, viewModel: OvertimeViewModel = viewModel()) {
    val currentMonth =
        remember { LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("es", "ES")) }

    val workDays by viewModel.workDays.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
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
            }
        }

        Button(
            onClick = { navController.navigate("addHrsExtrasScreen") },
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
                    weekDay = item.weekDay,
                    quantityOverHours = item.quantityOverHours,
                    percentageOverHours = item.percentageOverHours,
                    onDeleteConfirm = { /* Implementación de eliminación si es necesario */ }
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
                Text(text = weekDay, fontSize = 16.sp)
                Text(text = "Horas extras: $quantityOverHours", fontSize = 14.sp)
                Text(text = "Porcentaje: $percentageOverHours%", fontSize = 14.sp)
            }
            IconButton(onClick = { onDeleteConfirm() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}
