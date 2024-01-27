package com.delybills.makeaway.data.network.api

import com.delybills.makeaway.data.model.dto.task.IsCompletedDTO
import com.delybills.makeaway.data.model.dto.task.TaskKanbanDTO
import com.delybills.makeaway.data.model.dto.task.TaskMatrixDTO
import com.delybills.makeaway.data.model.dto.task.TaskToDoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DeskApi {

    @GET("/desks/todo/{id}")
    suspend fun getTODOTasks(
        @Path("id") id: String
    ): List<TaskToDoDTO>
    @GET("/desks/kanban/{id}")
    suspend fun getKanbanTasks(
        @Path("id") id: String
    ): List<TaskKanbanDTO>
    @GET("/desks/matrix/{id}")
    suspend fun getMatrixTasks(
        @Path("id") id: String
    ): List<TaskMatrixDTO>

    @POST("/tasks/{id}")
    suspend fun markTask(
        @Path("id") id: String,
        @Body body: IsCompletedDTO,
    )
}