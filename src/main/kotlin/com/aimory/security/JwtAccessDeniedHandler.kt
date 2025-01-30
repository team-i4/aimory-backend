package com.aimory.security

import com.aimory.controller.dto.ApiErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(
    private val om: ObjectMapper,
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.contentType = "application/json; charset=UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN // 403 Forbidden
        response.writer.write(
            om.writeValueAsString(
                ApiErrorResponse("Authentication error (forbidden)", HttpStatus.FORBIDDEN)
            )
        )
        response.writer.flush()
        response.writer.close()
    }
}
