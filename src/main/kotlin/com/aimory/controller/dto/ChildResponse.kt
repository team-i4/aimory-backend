package com.aimory.controller.dto

import com.aimory.model.Child

data class ChildResponse(
    val id: Long,
    val parentId: Long,
    val profileImageUrl: String?,
)

fun Child.toChildResponse() = ChildResponse(
    id = id,
    parentId = parent.id,
    profileImageUrl = profileImageUrl
)
