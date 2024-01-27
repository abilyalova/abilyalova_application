package com.delybills.makeaway.data.network.api

import com.delybills.makeaway.data.model.dto.auth.UserLoginDTO
import com.delybills.makeaway.data.model.dto.auth.UserRegisterDTO
import com.delybills.makeaway.data.model.dto.response.AuthResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/register")
    suspend fun registerUser(@Body requestBody: UserRegisterDTO): AuthResponseDTO

    @POST("/login")
    suspend fun loginUser(@Body requestBody: UserLoginDTO): AuthResponseDTO
}


