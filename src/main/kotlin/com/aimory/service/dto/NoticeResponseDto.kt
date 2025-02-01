package com.aimory.service.dto

import com.aimory.model.Notice
import java.time.LocalDate

data class NoticeResponseDto(
    val id: Long,
    val centerId: Long,
    val title: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

fun Notice.toResponseDto() = NoticeResponseDto(
    id = id,
    centerId = center.id,
    title = title,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
