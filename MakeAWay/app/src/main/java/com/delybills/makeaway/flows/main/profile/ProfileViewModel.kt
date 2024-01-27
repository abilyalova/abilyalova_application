package com.delybills.makeaway.flows.main.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : BaseViewModel(repo) {
    private val _messageDataGetUserInfo = MutableSharedFlow<User>()
    val messageDataGetUserInfo: SharedFlow<User> = _messageDataGetUserInfo.asSharedFlow()

    init {
        getUserInfo()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            when (val result = repo.getUserInfo()) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageDataGetUserInfo.emit(result.value)

                }
                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }
}