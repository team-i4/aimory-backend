package com.aimory.controller.dto

import com.aimory.service.dto.NoteResponseDto
import java.time.LocalDate
import java.time.LocalDateTime

data class NoteResponse(
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

data class NoteListResponse(
    val notes: List<NoteResponse>,
)

fun NoteResponseDto.toResponse() = NoteResponse(
    id = id,
    childId = childId,
    childName = childName,
    childClass = childClass,
    image = image,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt
)
