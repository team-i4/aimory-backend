package com.aimory.controller.dto

import com.aimory.service.dto.PhotoResponseDto

data class PhotoResponse(
    val photoId: Long,
    val imageUrl: String,
    val childId: Long,
    val childName: String,
    val createdAt: String,
)

data class PhotoListResponse(
    val totalCount: Int,
    val photos: List<PhotoResponse>,
)

fun PhotoResponseDto.toResponse() = PhotoResponse(
    photoId = photoId,
    imageUrl = imageUrl,
    childId = childId,
    childName = childName,
    createdAt = createdAt
)

fun List<PhotoResponseDto>.toResponse(): List<PhotoResponse> {
    return this.map { it.toResponse() }
}
