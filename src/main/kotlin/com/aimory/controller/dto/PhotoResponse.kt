package com.aimory.controller.dto

import com.aimory.enums.PhotoStatus
import com.aimory.service.dto.PhotoResponseDto
import java.time.LocalDateTime

data class PhotoResponse(
    val photoId: Long,
    val imageUrl: String,
    val childIds: List<Long>,
    val childNames: List<String>,
    val createdAt: LocalDateTime,
    val status: PhotoStatus,
)

data class PhotoListResponse(
    val totalCount: Int,
    val photos: List<PhotoResponse>,
)

fun PhotoResponseDto.toResponse() = PhotoResponse(
    photoId = photoId,
    imageUrl = imageUrl,
    childIds = childIds,
    childNames = childNames,
    createdAt = createdAt,
    status = status
)

fun List<PhotoResponseDto>.toResponse(): List<PhotoResponse> {
    return this.map { it.toResponse() }
}
