package com.aimory.controller.dto

import com.aimory.service.dto.NoteImageRequestDto

data class NoteImageRequest(
    val childId: Long,
    val content: String,
)

fun NoteImageRequest.toRequestDto() = NoteImageRequestDto(
    childId = childId,
    content = content
)
