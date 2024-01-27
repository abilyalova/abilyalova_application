package com.delybills.makeaway.data.mapper.menu

import android.annotation.SuppressLint
import android.util.Log
import com.delybills.makeaway.data.mapper.desk.DeskDTOMapper
import com.delybills.makeaway.data.model.dto.desk.*
import com.delybills.makeaway.data.model.dto.response.MessageResponseDTO
import com.delybills.makeaway.data.model.dto.task.TaskToDoDTO
import com.delybills.makeaway.data.model.dto.task.TaskKanbanDTO
import com.delybills.makeaway.data.model.dto.task.TaskMatrixDTO
import com.delybills.makeaway.domain.model.desk.*
import com.delybills.makeaway.domain.model.response.MessageResponse
import com.delybills.makeaway.domain.model.task.kanban.TaskKanban
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrix
import com.delybills.makeaway.domain.model.task.todo.TaskToDo
import java.text.SimpleDateFormat
import java.util.*

class MenuDTOMapper {

    private val deskDTOMapper = DeskDTOMapper()

    fun deskDashboardDTOToDeskDashboard(desksDTO: DesksDTO): Desks =
        Desks(desksDTO.items.map { mapDeskDTOToDesk(it) })

    private fun mapDeskDTOToDesk(deskDTO: DeskDTO): Desk {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        var deadline: Date? = null
        try {
            deadline = deskDTO.deadline?.let { sdf.parse(it) }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return with(deskDTO) {
            Desk(
                id = id,
                name = name ?: "Доска",
                deskType = deskDTOMapper.mapStringToDeskType(deskType),
                deadline = deadline,
                description = description ?: ""
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun deskToDoToDeskToDoDTO(deskToDo: DeskToDo): DeskToDoDTO {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val dateFormatted = deskToDo.deadline?.let { sdf.format(it) }
        return with(deskToDo) {
            DeskToDoDTO(
                name = name,
                deskType = deskType,
                deadline = dateFormatted ?: "",
                description = description,
                tasks = tasksToDo?.items?.map(::mapTasksToDoToTasksToDoDTO)
            )
        }
    }

    private fun mapTasksToDoToTasksToDoDTO(taskToDo: TaskToDo): TaskToDoDTO {
        return with(taskToDo) {
            TaskToDoDTO(
                title = title,
                id = id,
                is_completed = isCompleted,
            )
        }
    }

    private fun mapTasksKanbanToTasksKanbanDTO(taskKanban: TaskKanban): TaskKanbanDTO {
        return with(taskKanban) {
            TaskKanbanDTO(
                id = id,
                title = title,
                status = status.toString(),
                is_completed = isCompleted,
            )
        }
    }

    private fun mapTasksMatrixToTasksMatrixDTO(taskMatrix: TaskMatrix): TaskMatrixDTO {
        return with(taskMatrix) {
            TaskMatrixDTO(
                id = id,
                title = title,
                category = category.toString(),
                is_completed = isCompleted,
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun deskKanbanToDeskKanbanDTO(deskKanban: DeskKanban): DeskKanbanDTO {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val dateFormatted = deskKanban.deadline?.let { sdf.format(it) }
        return with(deskKanban) {
            DeskKanbanDTO(
                name = name,
                deskType = deskType,
                deadline = dateFormatted ?: "",
                description = description,
                tasksKanban = tasksKanban?.items?.map(::mapTasksKanbanToTasksKanbanDTO)
            )
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun deskMatrixToDeskMatrixDTO(deskMatrix: DeskMatrix): DeskMatrixDTO {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val dateFormatted = deskMatrix.deadline?.let { sdf.format(it) }
        return with(deskMatrix) {
            DeskMatrixDTO(
                name = name,
                deskType = deskType,
                deadline = dateFormatted ?: "",
                description = description,
                tasksMatrix = tasksMatrix?.items?.map(::mapTasksMatrixToTasksMatrixDTO)
            )
        }
    }

    fun messageResponseToMessageResponseDTO(messageResponseDTO: MessageResponseDTO): MessageResponse =
        with(messageResponseDTO) {
            MessageResponse(
                message = id ?: ""
            )
        }
}