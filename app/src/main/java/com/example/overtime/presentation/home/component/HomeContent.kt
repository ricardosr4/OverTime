package com.example.overtime.presentation.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.overtime.data.model.WorkDay

@Composable
fun HomeContent(
    workDays: List<WorkDay>,
    onDeleteWorkDay: (WorkDay) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(workDays) { workDay ->
            WorkDayCard(
                workDay = workDay,
                onDeleteConfirm = { onDeleteWorkDay(workDay) }
            )
        }
    }
} 