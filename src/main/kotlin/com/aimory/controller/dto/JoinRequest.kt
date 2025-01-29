package com.aimory.controller.dto

import com.aimory.enums.Role
import com.aimory.service.dto.JoinRequestDto

data class JoinRequest(
    val centerId: Long,
    val email: String,
    val password: String,
    val name: String,
    val role: Role,
)

fun JoinRequest.toDto() =
    JoinRequestDto(
        centerId = centerId,
        email = email,
        password = password,
        name = name,
        role = role
    )
