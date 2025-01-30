package com.aimory.exception

open class AuthenticationException(message: String) : RuntimeException(message)

class UnauthorizedException(message: String?) : AuthenticationException("사용자 인증에 실패했습니다. : $message")
