package com.example.habittracker.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.habittracker.data.HabitStorage
import com.example.habittracker.model.Habit

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> get() = _habits

    private var nextId = 1

    init {
        val savedHabits = HabitStorage.loadHabits(getApplication())
        _habits.addAll(savedHabits)
        nextId = (savedHabits.maxOfOrNull { it.id } ?: 0) + 1
    }

    fun getHabitById(id: Int): Habit? {
        return _habits.find { it.id == id }
    }

    fun addHabit(title: String, description: String, isCompleted: Boolean = false) {
        if (title.isBlank()) return

        _habits.add(
            Habit(
                id = nextId++,
                title = title.trim(),
                description = description.trim(),
                isCompleted = isCompleted
            )
        )
        saveHabits()
    }

    fun updateHabit(id: Int, title: String, description: String, isCompleted: Boolean) {
        val index = _habits.indexOfFirst { it.id == id }
        if (index == -1 || title.isBlank()) return

        _habits[index] = _habits[index].copy(
            title = title.trim(),
            description = description.trim(),
            isCompleted = isCompleted
        )
        saveHabits()
    }

    fun deleteHabit(id: Int) {
        _habits.removeAll { it.id == id }
        saveHabits()
    }

    fun toggleCompleted(id: Int, completed: Boolean) {
        val index = _habits.indexOfFirst { it.id == id }
        if (index == -1) return

        _habits[index] = _habits[index].copy(isCompleted = completed)
        saveHabits()
    }

    private fun saveHabits() {
        HabitStorage.saveHabits(getApplication(), _habits)
    }
}