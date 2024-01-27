package com.delybills.makeaway.flows.main.createdesk.bottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delybills.makeaway.domain.model.task.kanban.TaskKanbanStatusType
import java.util.Calendar

class TaskKanbanViewModel: ViewModel() {
    var name = MutableLiveData<String>()
    var status = TaskKanbanStatusType.Unknown
}