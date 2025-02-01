package com.aimory.controller

import com.aimory.controller.dto.ChildRequest
import com.aimory.controller.dto.ChildResponse
import com.aimory.controller.dto.toDto
import com.aimory.security.JwtAuthentication
import com.aimory.service.ChildService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ChildController(
    private val childService: ChildService,
) {
    /**
     * 자녀 정보 생성
     */
    @PostMapping("/children")
    @ResponseStatus(HttpStatus.CREATED)
    fun createChild(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestBody childRequest: ChildRequest,
    ): ChildResponse {
        return childService.createChild(childRequest.toDto(authentication.id))
    }
}
