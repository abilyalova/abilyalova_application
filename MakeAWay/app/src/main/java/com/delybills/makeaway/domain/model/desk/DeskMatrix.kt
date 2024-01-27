package com.delybills.makeaway.domain.model.desk

import com.delybills.makeaway.domain.model.task.matrix.TasksMatrix
import java.util.*

class DeskMatrix(
    val name: String?,
    val deskType: DeskType,
    val deadline: Date?,
    val description: String?,
    val tasksMatrix: TasksMatrix?
)