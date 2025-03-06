package com.aimory.service.dto

import com.aimory.enums.PhotoStatus
import com.aimory.model.Photo
import java.time.LocalDateTime

data class PhotoResponseDto(
    val photoId: Long,
    val imageUrl: String,
    val childIds: List<Long>,
    val childNames: List<String>,
    val createdAt: LocalDateTime,
    val status: PhotoStatus,

    )

fun Photo.toResponseDto(): PhotoResponseDto {
    return PhotoResponseDto(
        photoId = this.id,
        imageUrl = this.imageUrl,
        childIds = this.photoChildren.map { it.child.id },
        childNames = this.photoChildren.map { it.child.name },
        createdAt = this.createdAt,
        status = this.status
    )
}

fun List<Photo>.toResponseDtoList(): List<PhotoResponseDto> {
    return this.map { it.toResponseDto() }
}
