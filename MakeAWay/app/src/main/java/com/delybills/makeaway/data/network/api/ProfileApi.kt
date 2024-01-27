package com.delybills.makeaway.data.network.api

import com.delybills.makeaway.data.model.dto.user.UserDTO
import retrofit2.http.GET

interface ProfileApi {

    @GET("/user/info")
    suspend fun getUserInfo(): UserDTO
}