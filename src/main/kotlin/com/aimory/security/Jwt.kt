package com.aimory.security

import com.aimory.enums.Role
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

class Jwt(
    private val issuer: String,
    private val clientSecret: String,
    private val expirySeconds: Int,
    private val algorithm: Algorithm = Algorithm.HMAC256(clientSecret),
    private val jwtVerifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build(),
) {
    fun newToken(claims: Claims): String {
        val now = Date()
        val builder = JWT.create()
        builder.withIssuer(issuer)
        builder.withIssuedAt(now)
        if (expirySeconds > 0) {
            builder.withExpiresAt(Date(now.time + expirySeconds * 1000L))
        }
        builder.withClaim("userKey", claims.userKey)
        builder.withClaim("name", claims.name)
        builder.withClaim("email", claims.email)
        builder.withClaim("role", claims.roles)
        return builder.sign(algorithm)
    }

    fun verify(token: String): Claims {
        return try {
            Claims(jwtVerifier.verify(token))
        } catch (e: JWTVerificationException) {
            throw JWTVerificationException("jwt verification failed", e)
        }
    }

    class Claims private constructor() {
        var userKey: Long? = null
        var name: String? = null
        var email: String? = null
        var roles: List<String?> = emptyList()
        var iat: Date? = null
        var exp: Date? = null

        constructor(decodedJWT: DecodedJWT) : this() {
            userKey = decodedJWT.getClaim("userKey")?.asLong()
            name = decodedJWT.getClaim("name")?.asString()
            email = decodedJWT.getClaim("email")?.asString()
            roles = decodedJWT.getClaim("role")?.asList(String::class.java) as List<String?>
            iat = decodedJWT.issuedAt
            exp = decodedJWT.expiresAt
        }

        fun iat(): Long {
            return iat?.time ?: -1
        }

        fun exp(): Long {
            return exp?.time ?: -1
        }

        companion object {
            fun of(userKey: Long, name: String, email: String, role: Role): Claims {
                val claims = Claims()
                claims.userKey = userKey
                claims.name = name
                claims.email = email
                claims.roles = listOf(role.value)
                return claims
            }
        }
    }
}
