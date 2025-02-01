package com.aimory.controller

import com.aimory.controller.dto.DeleteRequest
import com.aimory.controller.dto.DeleteResponse
import com.aimory.controller.dto.NoticeListResponse
import com.aimory.controller.dto.NoticeRequest
import com.aimory.controller.dto.NoticeResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.security.JwtAuthentication
import com.aimory.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "api_key")
@Tag(name = "notices", description = "공지사항 API")
class NoticeController(
    private val noticeService: NoticeService,
) {
    /**
     * 공지사항 생성
     */
    @PostMapping("/notices")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "공지사항 생성 API")
    fun createNotice(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestBody noticeRequest: NoticeRequest,
    ): NoticeResponse {
        val memberId = authentication.id
        val noticeDto = noticeService.createNotice(memberId, noticeRequest.toRequestDto())
        return noticeDto.toResponse()
    }

    /**
     * 공지사항 전체 조회
     */
    @GetMapping("/notices")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "공지사항 전체 조회 API")
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
    @Operation(summary = "공지사항 단일 조회 API")
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
    @Operation(summary = "공지사항 수정 API")
    fun updateNotice(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @PathVariable noticeId: Long,
        @RequestBody noticeRequest: NoticeRequest,
    ): NoticeResponse {
        val memberId = authentication.id
        val noticeDto = noticeService.updateNotice(memberId, noticeId, noticeRequest.toRequestDto())
        return noticeDto.toResponse()
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/notices")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "공지사항 삭제 API")
    fun deleteNotices(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestBody deleteRequest: DeleteRequest,
    ): DeleteResponse {
        val memberId = authentication.id
        val deleteNoticeIds = noticeService.deleteNotices(memberId, deleteRequest.data)
        return DeleteResponse(deleteNoticeIds)
    }
}
