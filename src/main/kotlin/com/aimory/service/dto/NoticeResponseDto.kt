package com.aimory.service.dto

import com.aimory.model.Notice
import java.time.LocalDate
import java.time.LocalDateTime

data class NoticeResponseDto(
    val id: Long,
    val centerId: Long,
    val images: List<String>,
    val title: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

fun Notice.toResponseDto() = NoticeResponseDto(
    id = id,
    centerId = center.id,
    images = noticeImages.map {
        it.imageUrl
    },
    title = title,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
