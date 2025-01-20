package com.aimory.controller.dto

import com.aimory.model.Photo

data class PhotoAlbumResponse(
    val childId: Long,
//    val name: String,
    val photoCount: Int,
    val photos: List<String>,
    val profileImageUrl: String
)
//fun List<Photo>.toPhotoAlbumResponse(childId: Long, name: String, profileImageUrl: String) =
fun List<Photo>.toPhotoAlbumResponse(childId: Long, profileImageUrl: String) =
    PhotoAlbumResponse(
        childId = childId,
//        name = name,
        photoCount = size,
        photos = map { it.imageUrl },
        profileImageUrl = profileImageUrl
    )
