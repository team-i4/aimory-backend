package com.aimory.controller.dto

import com.aimory.service.dto.NoticeRequestDto
import java.time.LocalDate

data class NoticeRequest(
    val title: String,
    val content: String,
    val date: LocalDate,
)

fun NoticeRequest.toRequestDto() = NoticeRequestDto(
    title = title,
    content = content,
    date = date
)
