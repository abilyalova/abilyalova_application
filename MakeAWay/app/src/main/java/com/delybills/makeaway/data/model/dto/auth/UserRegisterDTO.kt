package com.delybills.makeaway.data.model.dto.auth

data class UserRegisterDTO(
    val username: String,
    val email: String,
    val password: String
)