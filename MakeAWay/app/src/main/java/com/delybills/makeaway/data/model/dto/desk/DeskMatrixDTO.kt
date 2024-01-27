package com.delybills.makeaway.data.model.dto.desk

import com.delybills.makeaway.data.model.dto.task.TaskMatrixDTO
import com.delybills.makeaway.domain.model.desk.DeskType

class DeskMatrixDTO(
    val name: String?,
    val deskType: DeskType,
    val deadline: String?,
    val description: String?,
    val tasksMatrix: List<TaskMatrixDTO>?
)