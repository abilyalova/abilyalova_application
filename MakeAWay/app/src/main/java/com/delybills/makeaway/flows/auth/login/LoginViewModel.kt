package com.delybills.makeaway.flows.auth.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.login.UserLogin
import com.delybills.makeaway.domain.model.response.AuthResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repo: LoginRepository
) : BaseViewModel(repo) {

    private val _messageData = MutableSharedFlow<AuthResponse>()
    val messageDataLogin: SharedFlow<AuthResponse> = _messageData.asSharedFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = repo.LoginUser(
                UserLogin(
                    username,
                    password
                )
            )

            when (result) {
                is ApiResponse.OnSuccessResponse -> {
                    _messageData.emit(result.value)
                }
                is ApiResponse.OnErrorResponse -> {
                    Log.d("ERROR", result.body?.string() ?: "unknown error")
                    errorResponse.value = result
                }
            }
        }
    }
}