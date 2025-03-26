package com.aimory.service.dto

import com.aimory.enums.Role

data class MemberDto(
    val id: Long,
    val centerId: Long? = null,
    val email: String,
    val name: String,
    val role: Role,
)
