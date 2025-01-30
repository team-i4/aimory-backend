package com.aimory.controller.dto

import com.aimory.service.dto.AuthenticationDto
import com.aimory.service.dto.MemberDto
import com.aimory.service.dto.toMemberDto

class AuthenticationResponse() {
    lateinit var apiToken: String
    lateinit var member: MemberDto

    constructor(authenticationDto: AuthenticationDto) : this() {
        apiToken = authenticationDto.apiToken
        member = authenticationDto.toMemberDto()
    }
}
