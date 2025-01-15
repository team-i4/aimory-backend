package com.aimory.service.dto

import com.aimory.enums.Role
import com.aimory.model.Member

class JoinRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val role: Role,
)

fun JoinRequestDto.toEntity() = Member(
    email = email,
    password = password,
    name = name,
    role = role
)
