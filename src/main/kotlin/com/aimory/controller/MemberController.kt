package com.aimory.controller

import com.aimory.controller.dto.JoinRequest
import com.aimory.controller.dto.JoinResponse
import com.aimory.controller.dto.toDto
import com.aimory.service.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody joinRequest: JoinRequest,
    ): JoinResponse {
        return memberService.join(joinRequest.toDto())
    }
}
