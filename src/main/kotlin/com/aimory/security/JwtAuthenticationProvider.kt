package com.aimory.security

import com.aimory.controller.dto.AuthenticationRequest
import com.aimory.service.MemberService
import com.aimory.service.dto.AuthenticationDto
import org.apache.commons.lang3.ClassUtils
import org.springframework.dao.DataAccessException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils.createAuthorityList
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtAuthenticationProvider(
    private val jwt: Jwt,
    private val memberService: MemberService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val authenticationToken = authentication as JwtAuthenticationToken
        return processUserAuthentication(authenticationToken.authenticationRequest())
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return ClassUtils.isAssignable(JwtAuthenticationToken::class.java, authentication)
    }

    private fun processUserAuthentication(request: AuthenticationRequest): Authentication {
        try {
            val member = memberService.login(request.email, request.password)
            val authenticated = JwtAuthenticationToken(
                JwtAuthentication(member.id, member.name, member.email, member.role),
                null,
                createAuthorityList(member.role.value)
            )
            val apiToken: String = member.newApiToken(jwt, member.role)
            authenticated.details = AuthenticationDto(apiToken, member)
            return authenticated
        } catch (e: NoSuchElementException) {
            throw UsernameNotFoundException(e.message)
        } catch (e: IllegalArgumentException) {
            throw BadCredentialsException(e.message)
        } catch (e: DataAccessException) {
            throw AuthenticationServiceException(e.message, e)
        }
    }
}
