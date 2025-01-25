package com.aimory.controller.dto

import com.aimory.model.Photo

data class PhotoResponse(
    val photoId: Long,
    val imageUrl: String,
    val childId: Long,
)

fun Photo.toPhotoResponse(): PhotoResponse {
    return PhotoResponse(
        photoId = this.id,
        imageUrl = this.imageUrl,
        childId = this.child.id
    )
}
