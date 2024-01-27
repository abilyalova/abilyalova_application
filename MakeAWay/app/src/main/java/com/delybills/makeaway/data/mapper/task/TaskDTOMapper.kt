package com.delybills.makeaway.data.mapper.task

import android.util.Log
import com.delybills.makeaway.domain.model.task.kanban.TaskKanbanStatusType
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType

class TaskDTOMapper {
    fun mapStringToTaskStatus(status: String?): TaskKanbanStatusType = when (status) {
        TaskKanbanStatusType.ToDo.name -> TaskKanbanStatusType.ToDo
        TaskKanbanStatusType.InProgress.name -> TaskKanbanStatusType.InProgress
        TaskKanbanStatusType.Done.name -> TaskKanbanStatusType.Done
        else -> {
            Log.d("Error", "Неправильный статус")
            TaskKanbanStatusType.Unknown
        }

    }

    fun mapStringToTaskCategory(category: String?): TaskMatrixCategoryType = when (category) {
        TaskMatrixCategoryType.UI.name -> TaskMatrixCategoryType.UI
        TaskMatrixCategoryType.NUI.name -> TaskMatrixCategoryType.NUI
        TaskMatrixCategoryType.UNI.name -> TaskMatrixCategoryType.UNI
        TaskMatrixCategoryType.NUNI.name -> TaskMatrixCategoryType.NUNI
        else -> {
            Log.d("Error", "Неправильная категория")
            TaskMatrixCategoryType.Unknown
        }
    }
}