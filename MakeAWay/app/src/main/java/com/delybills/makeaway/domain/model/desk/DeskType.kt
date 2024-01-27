package com.delybills.makeaway.domain.model.desk

enum class DeskType(
    val title: String
) {
    Unknown(
        title = "Тип доски"
    ),
    TODO(
        title = "To-Do"
    ),
    Kanban(
        title = "Канбан"
    ),
    Matrix(
        title = "Матрица Эйзенхауэра"
    )
}