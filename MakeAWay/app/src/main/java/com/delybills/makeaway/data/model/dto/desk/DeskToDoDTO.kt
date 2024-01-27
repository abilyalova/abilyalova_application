package com.delybills.makeaway.data.model.dto.desk

import com.delybills.makeaway.data.model.dto.task.TaskToDoDTO
import com.delybills.makeaway.domain.model.desk.DeskType

class DeskToDoDTO(
    val name: String?,
    val deskType: DeskType,
    val deadline: String?,
    val description: String?,
    val tasks: List<TaskToDoDTO>?
)