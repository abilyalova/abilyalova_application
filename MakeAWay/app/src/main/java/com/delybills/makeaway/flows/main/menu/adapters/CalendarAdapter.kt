package com.delybills.makeaway.flows.main.menu.adapters

import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import com.delybills.makeaway.flows.main.menu.model.EventMenu
import java.util.UUID


class CalendarAdapter(
    private val onEventClicked: (EventMenu) -> Unit
) : WeekView.SimpleAdapter<EventMenu>() {
    override fun onCreateEntity(item: EventMenu): WeekViewEntity {
        return WeekViewEntity.Event.Builder(item)
            .setId(UUID.fromString(item.id).mostSignificantBits)
            .setTitle(item.title)
            .setStartTime(item.startTime)
            .setEndTime(item.endTime)
            .build()
    }

    override fun onEventClick(data: EventMenu) {
        onEventClicked(data)
        super.onEventClick(data)
    }
}