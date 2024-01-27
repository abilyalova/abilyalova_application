package com.delybills.makeaway.domain.model.task.kanban

import java.util.Calendar

class TaskKanban(
    val id: String,
    val title: String,
    val status: TaskKanbanStatusType,
    val isCompleted: Boolean,
)