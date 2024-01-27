package com.delybills.makeaway.data.model.dto.desk

import com.delybills.makeaway.data.model.dto.task.TaskKanbanDTO
import com.delybills.makeaway.domain.model.desk.DeskType

class DeskKanbanDTO(
    val name: String?,
    val deskType: DeskType,
    val deadline: String?,
    val description: String?,
    val tasksKanban: List<TaskKanbanDTO>?
)