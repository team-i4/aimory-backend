package com.aimory.controller

import com.aimory.controller.dto.NoticeCreateRequest
import com.aimory.controller.dto.NoticeResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.service.NoticeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeController(
    private val noticeService: NoticeService,
) {
    /**
     * 공지사항 생성
     */
    @PostMapping("/notices")
    @ResponseStatus(HttpStatus.CREATED)
    fun createNotice(@RequestBody noticeCreateRequest: NoticeCreateRequest): NoticeResponse {
        val noticeDto = noticeService.createNotice(noticeCreateRequest.toRequestDto())
        return noticeDto.toResponse()
    }
}
