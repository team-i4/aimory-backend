package com.aimory.controller.dto

import com.aimory.service.dto.NoteRequestDto
import java.time.LocalDate

data class NoteRequest(
    val image: String?,
    val childId: Long,
    val content: String,
    val date: LocalDate,
)

fun NoteRequest.toRequestDto() = NoteRequestDto(
    image = image,
    childId = childId,
    content = content,
    date = date
)
