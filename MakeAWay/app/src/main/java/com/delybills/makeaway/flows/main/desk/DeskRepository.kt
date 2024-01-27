package com.delybills.makeaway.flows.main.desk

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.task.TaskDTOMapper
import com.delybills.makeaway.data.model.dto.task.IsCompletedDTO
import com.delybills.makeaway.data.network.api.DeskApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrix
import com.delybills.makeaway.domain.model.task.todo.TaskToDo

class DeskRepository(
    val api: DeskApi
) : BaseRepository() {

    private val mapper = TaskDTOMapper()

    suspend fun getTODOTasks(id: String): ApiResponse<List<TaskToDo>> {
        val response = protectedApiCall {
            api.getTODOTasks(id)
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(response.value.map { task ->
                TaskToDo(
                    task.id,
                    task.title,
                    task.is_completed,
                )
            })

            is ApiResponse.OnErrorResponse -> response

        }

        return result
    }

    suspend fun getKanbanTasks(id: String): ApiResponse<List<TaskKanban>> {
        val response = protectedApiCall {
            api.getKanbanTasks(id)
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(response.value.map { task ->
                TaskKanban(
                    task.id,
                    task.title,
                    mapper.mapStringToTaskStatus(task.status),
                    task.is_completed,
                )
            })

            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }


    suspend fun getMatrixTasks(id: String): ApiResponse<List<TaskMatrix>> {
        val response = protectedApiCall {
            api.getMatrixTasks(id)
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(response.value.map { task ->
                TaskMatrix(
                    task.id,
                    task.title,
                    mapper.mapStringToTaskCategory(task.category),
                    task.is_completed,
                )
            })

            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }

    suspend fun markTask(taskId: String, isCompleted: Boolean): ApiResponse<Unit> {
        val response = protectedApiCall {
            api.markTask(taskId, IsCompletedDTO(
                isCompleted
            ))
        }
        return response
    }
}