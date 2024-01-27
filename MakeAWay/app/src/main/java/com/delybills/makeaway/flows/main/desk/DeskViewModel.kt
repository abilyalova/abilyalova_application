package com.delybills.makeaway.flows.main.desk

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrix
import com.delybills.makeaway.domain.model.task.todo.TaskToDo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeskViewModel @Inject constructor(
    private val repo: DeskRepository
) : BaseViewModel(repo) {

    private val _todoTasks = MutableSharedFlow<List<TaskToDo>>()
    val todoTasks: SharedFlow<List<TaskToDo>> = _todoTasks.asSharedFlow()

    private val _kanbanTasks = MutableSharedFlow<List<TaskKanban>>()
    val kanbanTasks: SharedFlow<List<TaskKanban>> = _kanbanTasks.asSharedFlow()

    private val _matrixTasks = MutableSharedFlow<List<TaskMatrix>>()
    val matrixTasks: SharedFlow<List<TaskMatrix>> = _matrixTasks.asSharedFlow()

    fun getToDoTasks(id: String) {
        viewModelScope.launch {
            when (val response = repo.getTODOTasks(id)) {
                is ApiResponse.OnSuccessResponse -> {
                    _todoTasks.emit(response.value)
                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", response.body?.string() ?: "unknown error")
                    errorResponse.value = response
                }
            }
        }
    }

    fun getKanbanTasks(id: String) {
        viewModelScope.launch {
            when (val response = repo.getKanbanTasks(id)) {
                is ApiResponse.OnSuccessResponse -> {
                    _kanbanTasks.emit(response.value)
                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", response.body?.string() ?: "unknown error")
                    errorResponse.value = response
                }
            }
        }
    }

    fun getMatrixTasks(id: String) {
        viewModelScope.launch {
            when (val response = repo.getMatrixTasks(id)) {
                is ApiResponse.OnSuccessResponse -> {
                    _matrixTasks.emit(response.value)
                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", response.body?.string() ?: "unknown error")
                    errorResponse.value = response
                }
            }
        }
    }

    fun setTaskCompletion(taskId: String, isCompleted: Boolean){
        viewModelScope.launch {
            repo.markTask(taskId, isCompleted)
        }
    }
}