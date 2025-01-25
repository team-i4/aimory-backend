package com.aimory.service.dto

import com.aimory.model.Notice
import java.time.LocalDate

data class NoticeCreateRequestDto(
    val title: String,
    val content: String,
    val date: LocalDate,
)

fun NoticeCreateRequestDto.toEntity() = Notice(
    title = title,
    content = content,
    date = date
)
