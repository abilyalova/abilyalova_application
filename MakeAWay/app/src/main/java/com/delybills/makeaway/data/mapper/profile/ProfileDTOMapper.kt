package com.delybills.makeaway.data.mapper.profile

import com.delybills.makeaway.data.model.dto.user.UserDTO
import com.delybills.makeaway.domain.model.User

class ProfileDTOMapper {
    fun userDTOToUser(userDTO: UserDTO): User =
        with(userDTO) {
            User(
                username = username,
                email = email
            )
        }
}
