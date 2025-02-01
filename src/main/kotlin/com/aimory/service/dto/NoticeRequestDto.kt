package com.aimory.service.dto

import com.aimory.model.Center
import com.aimory.model.Notice
import java.time.LocalDate

data class NoticeRequestDto(
    val title: String,
    val content: String,
    val date: LocalDate,
)

fun NoticeRequestDto.toEntity(center: Center) = Notice(
    center = center,
    title = title,
    content = content,
    date = date
)
