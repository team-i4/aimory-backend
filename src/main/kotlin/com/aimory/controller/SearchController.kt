package com.aimory.controller

import com.aimory.controller.dto.SearchRequest
import com.aimory.controller.dto.SearchResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.security.JwtAuthentication
import com.aimory.service.SearchService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@SecurityRequirement(name = "Authorization")
class SearchController(
    private val searchService: SearchService,
) {
    @PostMapping("/search")
    fun search(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestBody searchRequest: SearchRequest,
    ): Mono<SearchResponse> {
        val memberId = authentication.id
        val memberRole = authentication.role
        val content = searchService.search(memberId, memberRole, searchRequest.toRequestDto())
        return content.map {
                it ->
            SearchResponse(it)
        }
    }
}
