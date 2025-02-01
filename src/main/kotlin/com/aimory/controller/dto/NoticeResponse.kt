package com.aimory.controller.dto

import com.aimory.service.dto.NoticeResponseDto
import java.time.LocalDate

data class NoticeResponse(
    val id: Long,
    val centerId: Long,
    val title: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

data class NoticeListResponse(
    val notices: List<NoticeResponse>,
)

fun NoticeResponseDto.toResponse() = NoticeResponse(
    id = id,
    centerId = centerId,
    title = title,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
