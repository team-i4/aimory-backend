package com.aimory.security

import com.aimory.enums.Role
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.GenericFilterBean
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.regex.Pattern
import java.util.stream.Collectors

class JwtAuthenticationTokenFilter(
    private val headerKey: String,
    private val jwt: Jwt,
) : GenericFilterBean() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val BEARER: Pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE)

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request: HttpServletRequest = req as HttpServletRequest
        val response: HttpServletResponse = res as HttpServletResponse

        if (SecurityContextHolder.getContext().authentication == null) {
            val authorizationToken = obtainAuthorizationToken(request)
            log.info("authorizationToken: {}", authorizationToken)
            // JWT 값이 있으면 JWT 값을 검증한 후 인증정보를 생성하여 SecurityContextHolder 에 추가
            if (authorizationToken != null) {
                try {
                    val claims = verify(authorizationToken)
                    val userKey: Long? = claims.userKey
                    val name: String? = claims.name
                    val email: String? = claims.email

                    val authorities = obtainAuthorities(claims)
                    val role = Role.getRole(authorities[0]?.authority)
                    if (userKey != null && !name.isNullOrBlank() && !email.isNullOrBlank() && role != null) {
                        val authentication =
                            JwtAuthenticationToken(JwtAuthentication(userKey, name, email, role), null, authorities)
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                } catch (e: Exception) {
                    log.info("Jwt processing failed: {}", e.message)
                }
            }
        } else {
            log.debug(
                "SecurityContextHolder 에 인증 정보가 존재합니다: '{}'",
                SecurityContextHolder.getContext().authentication
            )
        }

        chain.doFilter(request, response)
    }

    private fun obtainAuthorities(claims: Jwt.Claims): List<GrantedAuthority?> {
        val roles = claims.roles
        return if (roles.isEmpty()) {
            emptyList<GrantedAuthority>()
        } else {
            roles.stream()
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        }
    }

    private fun obtainAuthorizationToken(request: HttpServletRequest): String? {
        var token: String? = request.getHeader(headerKey)
        log.info("token : {}", token)
        if (token != null) {
            try {
                token = URLDecoder.decode(token, "UTF-8")
                val parts = token.split(" ")
                if (parts.size == 2) {
                    val scheme = parts[0]
                    val credentials = parts[1]
                    return if (BEARER.matcher(scheme).matches()) credentials else null
                }
            } catch (e: UnsupportedEncodingException) {
                log.error(e.message, e)
            }
        }
        return null
    }

    private fun verify(token: String): Jwt.Claims {
        return jwt.verify(token)
    }
}
