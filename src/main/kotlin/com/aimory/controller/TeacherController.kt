package com.aimory.controller

import com.aimory.controller.dto.TeacherResponse
import com.aimory.controller.dto.toResponse
import com.aimory.security.JwtAuthentication
import com.aimory.service.TeacherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "teacher", description = "선생님 화면 API")
class TeacherController(
    private val teacherService: TeacherService,
) {
    /**
     * 선생님 정보 조회
     */
    @GetMapping("/teacher")
    @Operation(summary = "선생님 화면의 내 정보 조회 API")
    fun getDetailTeacher(
        @AuthenticationPrincipal authentication: JwtAuthentication,
    ): TeacherResponse {
        return teacherService.getDetailTeacher(authentication.id).toResponse()
    }
}
