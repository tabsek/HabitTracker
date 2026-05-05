package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.ui.HabitDetailScreen
import com.example.habittracker.viewmodel.HabitViewModel

class HabitDetailFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val habitId = arguments?.getInt("habitId", -1) ?: -1
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val habit = viewModel.getHabitById(habitId)
                val isEditMode = habit != null

                HabitDetailScreen(
                    initialTitle = habit?.title.orEmpty(),
                    initialDescription = habit?.description.orEmpty(),
                    initialCompleted = habit?.isCompleted ?: false,
                    isEditMode = isEditMode,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaveClick = { title, description, completed ->
                        if (isEditMode) {
                            viewModel.updateHabit(
                                id = habitId,
                                title = title,
                                description = description,
                                isCompleted = completed
                            )
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbar_message", "Изменения сохранены")
                        } else {
                            viewModel.addHabit(
                                title = title,
                                description = description,
                                isCompleted = completed
                            )
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbar_message", "Привычка добавлена")
                        }

                        navController.popBackStack()
                    }
                )
            }
        }
    }
}