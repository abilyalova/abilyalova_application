package com.delybills.makeaway.data.network.api

import com.delybills.makeaway.data.model.dto.desk.*
import com.delybills.makeaway.data.model.dto.response.MessageResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MenuApi {

    @GET("/get_desks")
    suspend fun getDashboards(): DesksDTO

    @POST("/save_desk")
    suspend fun saveDesk(@Body requestBody: DeskDTO): MessageResponseDTO

    @POST("/desk/todo")
    suspend fun saveToDoDesk(@Body requestBody: DeskToDoDTO): MessageResponseDTO

    @POST("/desk/kanban")
    suspend fun saveKanbanDesk(@Body requestBody: DeskKanbanDTO): MessageResponseDTO

    @POST("/desk/matrix")
    suspend fun saveMatrixDesk(@Body requestBody: DeskMatrixDTO): MessageResponseDTO
}