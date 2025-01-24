package com.aimory.controller.dto

import com.aimory.model.Photo

data class PhotoListResponse(
    val childId: Long,
    val photoCount: Int,
    val photos: List<PhotoResponse>,
)

fun List<Photo>.toPhotoListResponse(childId: Long, photoCount: Int): PhotoListResponse {
    return PhotoListResponse(
        childId = childId,
        photoCount = photoCount,
        photos = this.map { it.toPhotoResponse() }
    )
}
