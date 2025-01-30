package com.aimory.controller.dto

data class AuthenticationRequest(
    val email: String,
    val password: String,
)
