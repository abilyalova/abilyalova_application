package com.delybills.makeaway.flows.main.pomodoro

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.desk.Desks
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PomodoroViewModel @Inject constructor(
    private val repo: PomodoroRepository
) : BaseViewModel(repo) {

    private val _messageDataGetDesks = MutableSharedFlow<Desks>()
    val messageDataGetDesks: SharedFlow<Desks> = _messageDataGetDesks.asSharedFlow()

    fun getDesks() {
        viewModelScope.launch {
            when (val result = repo.getDesks()) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageDataGetDesks.emit(result.value)

                }
                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }
}