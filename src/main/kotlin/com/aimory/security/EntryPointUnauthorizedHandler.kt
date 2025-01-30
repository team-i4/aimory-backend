package com.aimory.security

import com.aimory.controller.dto.ApiErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class EntryPointUnauthorizedHandler(
    private val om: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.contentType = "application/json; charset=UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED // 401 Unauthorized
        response.writer.write(
            om.writeValueAsString(
                ApiErrorResponse("Authentication error (unauthorized)", HttpStatus.UNAUTHORIZED)
            )
        )
        response.writer.flush()
        response.writer.close()
    }
}
