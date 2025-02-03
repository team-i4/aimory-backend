package com.aimory.service.dto

import com.aimory.model.Photo

data class PhotoResponseDto(
    val photoId: Long,
    val imageUrl: String,
    val childId: Long,
    val childName: String,
    val createdAt: String,

)

fun Photo.toResponseDto(): PhotoResponseDto {
    return PhotoResponseDto(
        photoId = this.id,
        imageUrl = this.imageUrl,
        childId = this.child.id,
        childName = this.child.name,
        createdAt = this.createdAt.toString()
    )
}

fun List<Photo>.toResponseDtoList(): List<PhotoResponseDto> {
    return this.map { it.toResponseDto() }
}
