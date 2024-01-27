package com.delybills.makeaway.flows.main.createdesk.bottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delybills.makeaway.domain.model.task.matrix.TaskMatrixCategoryType
import java.util.Calendar

class TaskMatrixViewModel: ViewModel() {
    var name = MutableLiveData<String>()
    var category = TaskMatrixCategoryType.Unknown
}