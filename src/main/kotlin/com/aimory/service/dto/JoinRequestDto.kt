package com.aimory.service.dto

import com.aimory.enums.Role
import com.aimory.model.Member
import com.aimory.model.Parent
import com.aimory.model.Teacher

class JoinRequestDto(
    val centerId: Long,
    val email: String,
    val password: String,
    val name: String,
    val role: Role,
)

fun JoinRequestDto.toEntity(): Member {
    val member =
        if (role == Role.TEACHER) {
            Teacher(
                centerId = centerId,
                email = email,
                password = password,
                name = name
            )
        } else {
            Parent(
                centerId = centerId,
                email = email,
                password = password,
                name = name
            )
        }
    return member
}
