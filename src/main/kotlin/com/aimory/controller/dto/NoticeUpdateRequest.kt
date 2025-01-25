package com.aimory.controller.dto

import com.aimory.service.dto.NoticeUpdateRequestDto
import java.time.LocalDate

data class NoticeUpdateRequest(
    val title: String,
    val content: String,
    val date: LocalDate,
)

fun NoticeUpdateRequest.toRequestDto() = NoticeUpdateRequestDto(
    title = title,
    content = content,
    date = date
)
