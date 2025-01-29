package com.aimory.controller.dto

import com.aimory.service.dto.NoteRequestDto
import java.time.LocalDate

data class NoteRequest(
    val content: String,
    val date: LocalDate,
)

fun NoteRequest.toRequestDto() = NoteRequestDto(
    content = content,
    date = date
)
