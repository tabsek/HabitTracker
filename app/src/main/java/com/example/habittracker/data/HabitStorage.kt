package com.example.habittracker.data

import android.content.Context
import com.example.habittracker.model.Habit
import org.json.JSONArray
import org.json.JSONObject

object HabitStorage {

    private const val PREFS_NAME = "habit_prefs"
    private const val KEY_HABITS = "habits"

    fun saveHabits(context: Context, habits: List<Habit>) {
        val jsonArray = JSONArray()

        habits.forEach { habit ->
            val jsonObject = JSONObject().apply {
                put("id", habit.id)
                put("title", habit.title)
                put("description", habit.description)
                put("isCompleted", habit.isCompleted)
            }
            jsonArray.put(jsonObject)
        }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_HABITS, jsonArray.toString())
            .apply()
    }

    fun loadHabits(context: Context): List<Habit> {
        val jsonString = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_HABITS, null)
            ?: return emptyList()

        return try {
            val jsonArray = JSONArray(jsonString)
            buildList {
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    add(
                        Habit(
                            id = item.getInt("id"),
                            title = item.getString("title"),
                            description = item.getString("description"),
                            isCompleted = item.getBoolean("isCompleted")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}