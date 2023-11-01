package com.delybills.makeaway.flows.auth.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.login.UserRegister
import com.delybills.makeaway.domain.model.response.AuthResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val repo: RegisterRepository
) : BaseViewModel(repo) {

    private val _messageData = MutableSharedFlow<AuthResponse>()
    val messageDataRegister: SharedFlow<AuthResponse> = _messageData.asSharedFlow()

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repo.registerUser(
                UserRegister(
                    username,
                    email,
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
