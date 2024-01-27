package com.delybills.makeaway.flows.main.menu.model

import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import java.util.Calendar

data class EventMenu(
    val id: String,
    val title: String,
    val startTime: Calendar,
    val endTime: Calendar
)