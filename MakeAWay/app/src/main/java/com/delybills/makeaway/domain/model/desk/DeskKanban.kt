package com.delybills.makeaway.domain.model.desk

import com.delybills.makeaway.domain.model.task.kanban.TasksKanban
import java.util.*

class DeskKanban(
    val name: String?,
    val deskType: DeskType,
    val deadline: Date?,
    val description: String?,
    val tasksKanban: TasksKanban?

)