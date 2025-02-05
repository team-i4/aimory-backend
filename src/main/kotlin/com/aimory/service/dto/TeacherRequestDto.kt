package com.aimory.service.dto

import com.aimory.model.Classroom

data class TeacherRequestDto(
    val profileImageUrl: String?,
    val classroom: Classroom?,
)
