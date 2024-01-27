package com.delybills.makeaway.domain.model.task.matrix

class TaskMatrix(
    val id: String,
    val title: String,
    val category: TaskMatrixCategoryType,
    val isCompleted: Boolean,
)