package com.example.habittracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittracker.model.Habit
import com.example.habittracker.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel
) {
    val habits = viewModel.habits

    var showAddDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Трекер привычек") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить привычку"
                )
            }
        }
    ) { innerPadding ->

        if (habits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Пока нет привычек.\nНажми +, чтобы добавить.",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(habits, key = { it.id }) { habit ->
                    HabitCard(
                        habit = habit,
                        onCheckedChange = { checked ->
                            viewModel.toggleCompleted(habit.id, checked)
                        },
                        onEditClick = {
                            editingHabit = habit
                        },
                        onDeleteClick = {
                            viewModel.deleteHabit(habit.id)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        HabitDialog(
            title = "Добавить привычку",
            initialTitle = "",
            initialDescription = "",
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description ->
                viewModel.addHabit(title, description)
                showAddDialog = false
            }
        )
    }

    editingHabit?.let { habit ->
        HabitDialog(
            title = "Редактировать привычку",
            initialTitle = habit.title,
            initialDescription = habit.description,
            onDismiss = { editingHabit = null },
            onConfirm = { newTitle, newDescription ->
                viewModel.updateHabit(habit.id, newTitle, newDescription)
                editingHabit = null
            }
        )
    }
}