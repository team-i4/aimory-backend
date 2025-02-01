package com.aimory.controller.dto

import com.aimory.service.dto.ChildRequestDto

data class ChildRequest(
    val name: String,
    val profileImageUrl: String?,
)

fun ChildRequest.toDto(parentId: Long) = ChildRequestDto(
    name = name,
    parentId = parentId,
    profileImageUrl = profileImageUrl
)
