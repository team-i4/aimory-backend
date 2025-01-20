package com.aimory.controller.dto

import com.aimory.model.Child
import com.aimory.model.Photo

data class PhotoRequest(
    val imageUrl: String,
    val childId: Long
)

fun PhotoRequest.toEntity(child: Child) =
    Photo(
        imageUrl = imageUrl,
        child = child
    )
