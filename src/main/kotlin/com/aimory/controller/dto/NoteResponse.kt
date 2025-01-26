package com.aimory.controller.dto

import com.aimory.service.dto.NoteResponseDto
import java.time.LocalDate

data class NoteResponse(
    val id: Long,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

data class NoteListResponse(
    val notes: List<NoteResponse>,
)

fun NoteResponseDto.toResponse() = NoteResponse(
    id = id,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
