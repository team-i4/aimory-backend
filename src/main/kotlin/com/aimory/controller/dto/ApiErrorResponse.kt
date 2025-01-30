package com.aimory.controller.dto

import org.springframework.http.HttpStatus

class ApiErrorResponse(
    message: String?,
    status: HttpStatus,
) {
    var message = message
    var status = status.value()

    constructor(throwable: Throwable, status: HttpStatus) : this(throwable.message, status) {
        this.status = status.value()
    }
}
