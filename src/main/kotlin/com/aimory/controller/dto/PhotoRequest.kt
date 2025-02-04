package com.aimory.controller.dto

data class PhotoRequest(
    val imageUrl: String,
)

data class AssignPhotoRequest(
    val photoIds: List<Long>,
    val childNames: List<String>,
)
