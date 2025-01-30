package com.aimory.service.dto

import com.aimory.model.Member

data class AuthenticationDto(
    val apiToken: String,
    val member: Member,
)

fun AuthenticationDto.toMemberDto() = MemberDto(
    id = member.id,
    centerId = member.centerId,
    email = member.email,
    name = member.name,
    role = member.role
)
