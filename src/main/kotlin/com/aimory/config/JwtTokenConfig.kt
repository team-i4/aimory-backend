package com.aimory.config

import com.aimory.security.Jwt
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean

@ConfigurationProperties(prefix = "jwt")
data class JwtTokenConfig(
    val header: String,
    val issuer: String,
    val clientSecret: String,
    val expirySeconds: Int,
) {
    @Bean
    fun jwt(jwtTokenConfigure: JwtTokenConfig): Jwt {
        return Jwt(
            jwtTokenConfigure.issuer,
            jwtTokenConfigure.clientSecret,
            jwtTokenConfigure.expirySeconds
        )
    }
}
