package com.delybills.makeaway.data.network.api

import com.delybills.makeaway.data.model.dto.response.TokensResponseDTO
import retrofit2.http.GET
import retrofit2.http.Header

interface TokensApi {

    @GET("/auth/refresh-token")
    suspend fun refreshAccessToken(
        @Header("Authorization") refresh_token: String
    ): TokensResponseDTO
}