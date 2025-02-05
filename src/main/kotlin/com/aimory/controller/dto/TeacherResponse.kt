package com.aimory.controller.dto

import com.aimory.service.dto.TeacherResponseDto

data class TeacherResponse(
    val id: Long,
    val centerId: Long,
    val classroomId: Long?,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
)

fun TeacherResponseDto.toResponse() = TeacherResponse(
    id = id,
    centerId = centerId,
    classroomId = classRoom?.id,
    name = name,
    email = email,
    profileImageUrl = profileImageUrl
)
