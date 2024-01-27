package com.delybills.makeaway.flows.main.createdesk

import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.data.mapper.menu.MenuDTOMapper
import com.delybills.makeaway.data.network.api.MenuApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import com.delybills.makeaway.domain.model.desk.DeskKanban
import com.delybills.makeaway.domain.model.desk.DeskMatrix
import com.delybills.makeaway.domain.model.desk.DeskToDo
import com.delybills.makeaway.domain.model.response.MessageResponse

class CreateDeskRepository(
    val api: MenuApi
) : BaseRepository() {
    private val mapper = MenuDTOMapper()
    suspend fun saveDeskToDo(deskToDo: DeskToDo): ApiResponse<MessageResponse> {
        val response = protectedApiCall {
            api.saveToDoDesk(mapper.deskToDoToDeskToDoDTO(deskToDo))
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.messageResponseToMessageResponseDTO(
                    response.value
                )
            )

            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }

    suspend fun saveDeskKanban(deskKanban: DeskKanban): ApiResponse<MessageResponse> {
        val response = protectedApiCall {
            api.saveKanbanDesk(mapper.deskKanbanToDeskKanbanDTO(deskKanban))
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.messageResponseToMessageResponseDTO(
                    response.value
                )
            )

            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }

    suspend fun saveDeskMatrix(deskMatrix: DeskMatrix): ApiResponse<MessageResponse> {
        val response = protectedApiCall {
            api.saveMatrixDesk(mapper.deskMatrixToDeskMatrixDTO(deskMatrix))
        }

        val result = when (response) {
            is ApiResponse.OnSuccessResponse -> ApiResponse.OnSuccessResponse(
                mapper.messageResponseToMessageResponseDTO(
                    response.value
                )
            )

            is ApiResponse.OnErrorResponse -> response
        }

        return result
    }
}