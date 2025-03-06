package com.aimory.service.dto

import com.aimory.model.Note
import java.time.LocalDate
import java.time.LocalDateTime

data class NoteResponseDto(
    val id: Long,
    val childId: Long,
    val childName: String,
    val childClass: String,
    val image: String?,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

fun Note.toResponseDto() = NoteResponseDto(
    id = id,
    childId = child.id,
    childName = child.name,
    childClass = child.classroom.name,
    image = noteImages.firstOrNull()?.imageUrl,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
