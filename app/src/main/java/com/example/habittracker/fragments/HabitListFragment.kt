package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.ui.HabitListScreen
import com.example.habittracker.viewmodel.HabitViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HabitListFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        val snackbarFlow = savedStateHandle?.getStateFlow<String?>("snackbar_message", null)
            ?: MutableStateFlow(null)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val snackbarMessage by snackbarFlow.collectAsState()

                HabitListScreen(
                    habits = viewModel.habits,
                    snackbarMessage = snackbarMessage,
                    onSnackbarShown = {
                        savedStateHandle?.set("snackbar_message", null)
                    },
                    onAddClick = {
                        navController.navigate(R.id.action_habitListFragment_to_habitDetailFragment)
                    },
                    onEditClick = { habitId ->
                        navController.navigate(
                            R.id.action_habitListFragment_to_habitDetailFragment,
                            bundleOf("habitId" to habitId)
                        )
                    },
                    onDeleteClick = { habitId ->
                        viewModel.deleteHabit(habitId)
                    },
                    onToggleCompleted = { habitId, completed ->
                        viewModel.toggleCompleted(habitId, completed)
                    }
                )
            }
        }
    }
}