package com.aimory.security

import com.aimory.controller.dto.AuthenticationRequest
import com.aimory.exception.UnauthorizedException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken : AbstractAuthenticationToken {

    private val principal: Any

    private val credentials: String?

    constructor(principal: String, credentials: String?) : super(null) {
        super.setAuthenticated(false)

        this.principal = principal
        this.credentials = credentials
    }

    constructor(principal: Any, credentials: String?, authorities: Collection<GrantedAuthority?>?) : super(
        authorities
    ) {
        super.setAuthenticated(true)

        this.principal = principal
        this.credentials = credentials
    }

    fun authenticationRequest(): AuthenticationRequest {
        credentials ?: throw UnauthorizedException("credentials is null")
        return AuthenticationRequest(principal.toString(), credentials)
    }

    override fun getPrincipal(): Any {
        return principal
    }

    override fun getCredentials(): String? {
        return credentials
    }

    // authenticated 는 생성자를 통해 초기화할 것
    override fun setAuthenticated(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            throw IllegalArgumentException()
        }
        super.setAuthenticated(false)
    }
}
