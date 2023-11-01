package com.delybills.makeaway.flows.main.pomodoro

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.menu.MenuDTOMapper
import com.delybills.makeaway.data.network.api.MenuApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.desk.Desks

class PomodoroRepository(
    val api: MenuApi
) : BaseRepository() {

    val mapper = MenuDTOMapper()

    suspend fun getDesks(): ApiResponse<Desks> {
        val response = protectedApiCall {
            api.getDashboards()
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.deskDashboardDTOToDeskDashboard(response.value)
            )
            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }
}