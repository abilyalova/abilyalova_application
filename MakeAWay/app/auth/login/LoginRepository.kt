package com.delybills.makeaway.flows.auth.login

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.auth.AuthDTOMapper
import com.delybills.makeaway.data.network.api.AuthApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.login.UserLogin
import com.delybills.makeaway.domain.model.response.AuthResponse

class LoginRepository(
    val api: AuthApi
) : BaseRepository() {
    private val mapper = AuthDTOMapper()
    suspend fun LoginUser(userLogin: UserLogin): ApiResponse<AuthResponse> {
        val response = protectedApiCall {
            api.loginUser(mapper.userLoginToUserLoginDTO(userLogin))
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