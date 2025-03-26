package com.aimory.service.dto

import com.aimory.enums.Role
import com.aimory.model.Director
import com.aimory.model.Member
import com.aimory.model.Parent
import com.aimory.model.Teacher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class JoinRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val role: Role,
)

fun JoinRequestDto.toEntity(): Member {
    val password = BCryptPasswordEncoder().encode(password)
    val member =
        if (role == Role.TEACHER) {
            Teacher(
                email = email,
                password = password,
                name = name
            )
        } else if (role == Role.PARENT) {
            Parent(
                email = email,
                password = password,
                name = name
            )
        } else {
            Director(
                email = email,
                password = password,
                name = name
            )
        }
    return member
}
