package com.aimory.controller

import com.aimory.controller.dto.AuthenticationRequest
import com.aimory.controller.dto.AuthenticationResponse
import com.aimory.exception.UnauthorizedException
import com.aimory.security.JwtAuthenticationToken
import com.aimory.service.dto.AuthenticationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "authentication", description = "인증 API")
class AuthenticationController(private val authenticationManager: AuthenticationManager) {

    @PostMapping("/login")
    @Operation(summary = "로그인 API")
    fun authentication(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        try {
            val authToken: JwtAuthenticationToken = JwtAuthenticationToken(request.email, request.password)
            val authentication: Authentication = authenticationManager.authenticate(authToken)
            SecurityContextHolder.getContext().authentication = authentication
            return AuthenticationResponse(authentication.details as AuthenticationDto)
        } catch (e: AuthenticationException) {
            throw UnauthorizedException(e.message)
        }
    }
}
