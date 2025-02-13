package com.aimory.controller.dto

data class DeleteChildPhotoResponse(
    val deletedPhotosChildId: List<Long>,
)

data class DeletePhotoResponse(
    val deletedPhotoIds: List<Long>,
)

data class DeleteResponse(
    val data: List<Long>,
)
