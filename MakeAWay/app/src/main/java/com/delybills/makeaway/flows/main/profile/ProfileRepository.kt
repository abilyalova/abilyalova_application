package com.delybills.makeaway.flows.main.profile

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.profile.ProfileDTOMapper
import com.delybills.makeaway.data.network.api.ProfileApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.User

class ProfileRepository(
    val api: ProfileApi
) : BaseRepository() {
    val mapper = ProfileDTOMapper()

    suspend fun getUserInfo(): ApiResponse<User> {
        val response = protectedApiCall {
            api.getUserInfo()
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.userDTOToUser(response.value)
            )
            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }
}