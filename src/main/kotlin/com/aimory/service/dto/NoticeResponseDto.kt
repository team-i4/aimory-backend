package com.aimory.service.dto

import com.aimory.model.Notice
import java.time.LocalDate

data class NoticeResponseDto(
    val id: Long,
    val centerId: Long,
    val images: List<String>,
    val title: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
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
