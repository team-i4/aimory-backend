package com.aimory.controller.dto

import com.aimory.service.dto.NoticeCreateRequestDto
import java.time.LocalDate

data class NoticeCreateRequest(
    val title: String,
    val content: String,
    val date: LocalDate,
)

fun NoticeCreateRequest.toRequestDto() = NoticeCreateRequestDto(
    title = title,
    content = content,
    date = date
)
