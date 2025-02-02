package com.aimory.security

import com.aimory.enums.Role

/**
 * 인증된 사용자
 * */
class JwtAuthentication(
    val id: Long,
    val name: String,
    val email: String,
    val role: Role,
)
