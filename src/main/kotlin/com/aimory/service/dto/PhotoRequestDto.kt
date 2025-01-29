package com.aimory.service.dto

import com.aimory.model.Child
import com.aimory.model.Photo
import org.springframework.web.multipart.MultipartFile

data class PhotoRequestDto(
    val childId: Long,
    val imageUrls: List<String>,
)

fun List<MultipartFile>.toRequestDto(childId: Long): PhotoRequestDto {
    return PhotoRequestDto(
        childId = childId,
        imageUrls = this.map { it.originalFilename?.let { filename -> "local-storage/$filename" } ?: "" }
    )
}

fun PhotoRequestDto.toEntity(child: Child): List<Photo> {
    return imageUrls.map { url ->
        Photo(imageUrl = url, child = child)
    }
}
