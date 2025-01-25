package com.aimory.service.dto

import com.aimory.model.Note
import java.time.LocalDate

data class NoteRequestDto(
    val content: String,
    val date: LocalDate,
)

fun NoteRequestDto.toEntity() = Note(
    content = content,
    date = date
)
