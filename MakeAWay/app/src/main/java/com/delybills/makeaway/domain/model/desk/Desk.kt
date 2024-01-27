package com.delybills.makeaway.domain.model.desk

import java.io.Serializable
import java.util.Date

class Desk(
    val id: String,
    val name: String?,
    val deskType: DeskType,
    val deadline: Date?,
    val description: String?
): Serializable