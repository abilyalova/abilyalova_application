package com.delybills.makeaway.flows.main.menu

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.desk.Desks
import com.delybills.makeaway.flows.main.menu.model.EventMenu
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val repo: MenuRepository
) : BaseViewModel(repo) {

    private val _messageDataGetDesks = MutableSharedFlow<Desks>()
    val messageDataGetDesks: SharedFlow<Desks> = _messageDataGetDesks.asSharedFlow()

    private val _calendarEvents = MutableSharedFlow<List<EventMenu>>(replay = 1)
    val calendarEvents: SharedFlow<List<EventMenu>> = _calendarEvents.asSharedFlow()

    init {
        getDesks()
    }

    fun getDesks() {
        viewModelScope.launch {
            when (val result = repo.getDesks()) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageDataGetDesks.emit(result.value)
                    fillCalendar(result.value)
                }

                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }

    private fun fillCalendar(desks: Desks) {
        viewModelScope.launch {
            val events = desks.desks.mapNotNull { desk ->
                desk.deadline ?: return@mapNotNull null
                val currentCalendar = Calendar.getInstance()
                currentCalendar.time = desk.deadline
                currentCalendar.add(Calendar.HOUR, -1)
                val plusOneHour = Calendar.getInstance()
                plusOneHour.time = desk.deadline

                EventMenu(
                    id = desk.id,
                    startTime = currentCalendar,
                    endTime = plusOneHour,
                    title = desk.name ?: "Доска"
                )
            }
            _calendarEvents.emit(events)
        }
    }
}