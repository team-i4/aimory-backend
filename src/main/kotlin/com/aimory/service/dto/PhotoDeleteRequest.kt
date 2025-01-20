package com.aimory.service.dto

data class PhotoDeleteRequest(
    val type: String,
    val data: List<PhotoDeleteData>
)

data class PhotoDeleteData(
    val photoId: Long? = null,
    val childId: Long? = null
)
