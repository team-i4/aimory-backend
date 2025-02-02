package com.aimory.service.dto

import com.aimory.model.Note
import java.time.LocalDate

data class NoteResponseDto(
    val id: Long,
    val childId: Long,
    val childName: String,
    val childClass: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

fun Note.toResponseDto() = NoteResponseDto(
    id = id,
    childId = child.id,
    childName = child.name,
    childClass = child.classroom.name,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
