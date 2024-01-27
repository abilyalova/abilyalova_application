package com.delybills.makeaway.domain.model.task.kanban

enum class TaskKanbanStatusType(
    val title: String
){
    Unknown(
        title = "Статус"
    ),
    ToDo(
        title = "Выполнить"
    ),
    InProgress(
        title = "В процессе"
    ),
    Done(
        title = "Выполнено"
    )
}