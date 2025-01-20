package com.aimory.controller.dto

import com.aimory.model.Photo

data class PhotoResponse(
    val photoId: Long,
//    val name: String,
    val imageUrl: String,
    val childId: Long,
    val photoCount: Int

)

fun Photo.toPhotoResponse(photoCount: Int): PhotoResponse {
    return PhotoResponse(
        photoId = this.id,
//        name = this.child.name,
        imageUrl = this.imageUrl,
        childId = this.child.id,
        photoCount = photoCount
    )
}
