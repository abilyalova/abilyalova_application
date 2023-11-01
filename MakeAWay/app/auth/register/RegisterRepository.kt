package com.delybills.makeaway.flows.auth.register

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.auth.AuthDTOMapper
import com.delybills.makeaway.data.network.api.AuthApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.login.UserRegister
import com.delybills.makeaway.domain.model.response.AuthResponse

class RegisterRepository(
    val api: AuthApi
) : BaseRepository() {

    private val mapper = AuthDTOMapper()
    suspend fun registerUser(userRegister: UserRegister): ApiResponse<AuthResponse> {
        val response = protectedApiCall {
            api.registerUser(
                mapper.userRegisterToUserRegisterDTO(
                    userRegister
                )
            )
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.authResponseDTOToAuthResponse(
                    response.value
                )
            )
            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }
}