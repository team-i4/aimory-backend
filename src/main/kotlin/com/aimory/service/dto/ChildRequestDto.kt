package com.aimory.service.dto

import com.aimory.model.Child
import com.aimory.model.Parent

data class ChildRequestDto(
    val name: String,
    val parentId: Long,
    val profileImageUrl: String?,
)

fun ChildRequestDto.toEntity(parent: Parent) = Child(
    name = name,
    parent = parent,
    profileImageUrl = profileImageUrl
)
