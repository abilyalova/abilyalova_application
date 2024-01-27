package com.delybills.makeaway.data.mapper.desk

import android.util.Log
import com.delybills.makeaway.domain.model.desk.DeskType

class DeskDTOMapper {

    fun mapStringToDeskType(name: String?): DeskType = when (name) {
        DeskType.TODO.name -> DeskType.TODO
        DeskType.Kanban.name -> DeskType.Kanban
        DeskType.Matrix.name -> DeskType.Matrix
        else -> {
            Log.d("Error", "Неправильный тип доски")
            DeskType.Unknown
        }
    }
}