package com.aimory.service.dto

import com.aimory.model.Classroom
import com.aimory.model.Teacher

data class TeacherResponseDto(
    val id: Long,
    val centerId: Long?,
    val classRoom: Classroom?,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
)

fun Teacher.toResponseDto() = TeacherResponseDto(
    id = id,
    centerId = centerId,
    classRoom = classroom,
    name = name,
    email = email,
    profileImageUrl = profileImageUrl
)
