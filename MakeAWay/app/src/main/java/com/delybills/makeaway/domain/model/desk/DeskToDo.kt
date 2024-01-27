package com.delybills.makeaway.domain.model.desk

import com.delybills.makeaway.domain.model.task.todo.TasksToDo
import java.util.*

class DeskToDo(
    val name: String?,
    val deskType: DeskType,
    val deadline: Date?,
    val description: String?,
    val tasksToDo: TasksToDo?
)