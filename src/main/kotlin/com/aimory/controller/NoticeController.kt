package com.aimory.controller

import com.aimory.controller.dto.NoticeCreateRequest
import com.aimory.controller.dto.NoticeListResponse
import com.aimory.controller.dto.NoticeResponse
import com.aimory.controller.dto.NoticeUpdateRequest
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.service.NoticeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    fun createNotice(
        @RequestBody noticeCreateRequest: NoticeCreateRequest,
    ): NoticeResponse {
        val noticeDto = noticeService.createNotice(noticeCreateRequest.toRequestDto())
        return noticeDto.toResponse()
    }

    /**
     * 공지사항 전체 조회
     */
    @GetMapping("/notices")
    @ResponseStatus(HttpStatus.OK)
    fun getAllNotices():
        NoticeListResponse {
        val noticeListDto = noticeService.getAllNotices()
        val noticeListResponse = noticeListDto.map {
            it.toResponse()
        }
        return NoticeListResponse(notices = noticeListResponse)
    }

    /**
     * 공지사항 단일 조회
     */
    @GetMapping("/notices/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDetailNotice(
        @PathVariable noticeId: Long,
    ): NoticeResponse {
        val noticeDto = noticeService.getDetailNotice(noticeId)
        return noticeDto.toResponse()
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/notices/{noticeId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateNotice(
        @PathVariable noticeId: Long,
        @RequestBody noticeUpdateRequest: NoticeUpdateRequest,
    ): NoticeResponse {
        val noticeDto = noticeService.updateNotice(noticeId, noticeUpdateRequest.toRequestDto())
        return noticeDto.toResponse()
    }
}
