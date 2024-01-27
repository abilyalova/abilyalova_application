package com.delybills.makeaway.data.mapper.auth

import com.delybills.makeaway.data.model.dto.auth.UserLoginDTO
import com.delybills.makeaway.data.model.dto.auth.UserRegisterDTO
import com.delybills.makeaway.data.model.dto.response.AuthResponseDTO
import com.delybills.makeaway.domain.model.login.UserLogin
import com.delybills.makeaway.domain.model.login.UserRegister
import com.delybills.makeaway.domain.model.response.AuthResponse

class AuthDTOMapper {

    fun userRegisterToUserRegisterDTO(userRegister: UserRegister): UserRegisterDTO =
        with(userRegister) {
            UserRegisterDTO(
                username = username,
                email = email,
                password = password
            )
        }

    fun userLoginToUserLoginDTO(userLogin: UserLogin): UserLoginDTO =
        with(userLogin) {
            UserLoginDTO(
                username = username,
                password = password
            )
        }

    fun authResponseDTOToAuthResponse(authResponseDTO: AuthResponseDTO): AuthResponse =
        with(authResponseDTO) {
            AuthResponse(
                access_token = access_token,
                refresh_token = refresh_token
            )
        }

}