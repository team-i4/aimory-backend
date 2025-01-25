package com.aimory.service.dto

import java.time.LocalDate

data class NoticeUpdateRequestDto(
    val title: String,
    val content: String,
    val date: LocalDate,
)
