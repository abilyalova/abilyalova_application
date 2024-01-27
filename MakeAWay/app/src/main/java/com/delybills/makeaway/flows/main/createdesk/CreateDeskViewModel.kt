package com.delybills.makeaway.flows.main.createdesk

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.desk.*
import com.delybills.makeaway.domain.model.response.MessageResponse
import com.delybills.makeaway.domain.model.task.todo.TasksToDo
import com.delybills.makeaway.domain.model.task.kanban.TasksKanban
import com.delybills.makeaway.domain.model.task.matrix.TasksMatrix
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CreateDeskViewModel @Inject constructor(
    private val repo: CreateDeskRepository
) : BaseViewModel(repo) {

    private val _messageData = MutableSharedFlow<MessageResponse>()
    val messageDataCreateDesk: SharedFlow<MessageResponse> = _messageData.asSharedFlow()

    fun saveToDoDeskData(
        name: String, type: DeskType, deadline: Date?, description: String, tasks: TasksToDo
    ) {
        viewModelScope.launch {
            val result = repo.saveDeskToDo(
                DeskToDo(
                    name, type, deadline, description, tasks
                )
            )

            when (result) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageData.emit(result.value)

                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }

    fun saveKanbanDeskData(
        name: String, type: DeskType, deadline: Date?, description: String, tasks: TasksKanban
    ) {
        viewModelScope.launch {
            val result = repo.saveDeskKanban(
                DeskKanban(
                    name, type, deadline, description, tasks
                )
            )

            when (result) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageData.emit(result.value)

                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }

    fun saveMatrixDeskData(
        name: String, type: DeskType, deadline: Date?, description: String, tasks: TasksMatrix
    ) {
        viewModelScope.launch {
            val result = repo.saveDeskMatrix(
                DeskMatrix(
                    name, type, deadline, description, tasks
                )
            )

            when (result) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageData.emit(result.value)

                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }
}

