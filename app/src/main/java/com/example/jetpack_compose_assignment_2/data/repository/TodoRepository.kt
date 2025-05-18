package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {
    val todos: Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun refreshTodos() {
        try {
            val todos = todoApi.getTodos()
            todoDao.deleteAllTodos()
            todoDao.insertTodos(todos)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }
} 