package com.example.todoapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel() {

    private var _uiState = MutableStateFlow<List<Task>>(emptyList())
    val uiState: StateFlow<List<Task>> = _uiState.asStateFlow()

    fun addTask(task: Task){
        _uiState.update { currentState ->
            val newState = currentState.plus(task)
            newState
        }
    }

    fun deleteTask(index: Int){
        _uiState.update { currentState ->
            Log.d("Task Index Delete: ", index.toString())
            val newState = currentState.filterIndexed { idx, task -> idx != index }
            Log.d("Delete Task", newState.toString())
            newState
        }
    }

    fun updateTask(index: Int, task: Task){
        _uiState.update { currentState ->
            val newState = currentState.toMutableList().apply {
                this[index] = task
            }
            newState
        }
    }
}