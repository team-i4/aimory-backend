package com.aimory.controller.dto

import com.aimory.service.dto.NoteImageRequestDto

data class NoteImageRequest(
    val content: String,
)

fun NoteImageRequest.toRequestDto() = NoteImageRequestDto(
    content = content
)
