package com.aimory.controller.dto

import com.aimory.enums.Role
import com.aimory.model.Member

data class JoinResponse(
    val id: Long,
    val centerId: Long,
    val email: String,
    val name: String,
    val role: Role,
)

fun Member.toJoinResponse() =
    JoinResponse(
        id = id,
        centerId = centerId,
        name = name,
        email = email,
        role = role
    )
