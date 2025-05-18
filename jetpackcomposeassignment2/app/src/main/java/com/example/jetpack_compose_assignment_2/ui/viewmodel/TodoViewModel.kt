package com.example.jetpack_compose_assignment_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            repository.todos.collect { todos ->
                _uiState.value = TodoUiState.Success(todos)
            }
        }
        refreshTodos()
    }

    fun refreshTodos() {
        viewModelScope.launch {
            try {
                repository.refreshTodos()
            } catch (e: Exception) {
                _uiState.value = TodoUiState.Error("Failed to refresh todos: ${e.message}")
            }
        }
    }

    fun getTodoById(id: Int): Todo? {
        return (uiState.value as? TodoUiState.Success)?.todos?.find { it.id == id }
    }
}

sealed class TodoUiState {
    data object Loading : TodoUiState()
    data class Success(val todos: List<Todo>) : TodoUiState()
    data class Error(val message: String) : TodoUiState()
} 