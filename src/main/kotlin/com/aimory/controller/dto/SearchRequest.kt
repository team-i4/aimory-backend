package com.aimory.controller.dto

import com.aimory.service.dto.SearchRequestDto

data class SearchRequest(
    val content: String,
)

fun SearchRequest.toRequestDto() = SearchRequestDto(
    content = content
)
