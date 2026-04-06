package com.example.habittracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.habittracker.model.Habit

class HabitViewModel : ViewModel() {

    private val _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> get() = _habits

    private var nextId = 1

    fun addHabit(title: String, description: String) {
        if (title.isBlank()) return

        _habits.add(
            Habit(
                id = nextId++,
                title = title.trim(),
                description = description.trim()
            )
        )
    }

    fun updateHabit(id: Int, title: String, description: String) {
        val index = _habits.indexOfFirst { it.id == id }
        if (index == -1 || title.isBlank()) return

        val oldHabit = _habits[index]
        _habits[index] = oldHabit.copy(
            title = title.trim(),
            description = description.trim()
        )
    }

    fun deleteHabit(id: Int) {
        _habits.removeAll { it.id == id }
    }

    fun toggleCompleted(id: Int, completed: Boolean) {
        val index = _habits.indexOfFirst { it.id == id }
        if (index == -1) return

        val oldHabit = _habits[index]
        _habits[index] = oldHabit.copy(isCompleted = completed)
    }
}