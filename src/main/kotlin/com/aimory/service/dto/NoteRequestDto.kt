package com.aimory.service.dto

import com.aimory.model.Child
import com.aimory.model.Note
import java.time.LocalDate

data class NoteRequestDto(
    val image: String?,
    val childId: Long,
    val content: String,
    val date: LocalDate,
)

fun NoteRequestDto.toEntity(child: Child) = Note(
    child = child,
    classroom = child.classroom,
    content = content,
    date = date
)
