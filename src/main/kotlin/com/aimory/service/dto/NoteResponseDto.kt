package com.aimory.service.dto

import com.aimory.model.Note
import java.time.LocalDate

data class NoteResponseDto(
    val id: Long,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

fun Note.toResponseDto() = NoteResponseDto(
    id = id,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
