package com.example.todoapp.viewModel

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
            val newState = currentState.drop(index)
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