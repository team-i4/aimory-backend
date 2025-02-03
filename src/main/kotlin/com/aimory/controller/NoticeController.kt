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
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "notices", description = "공지사항 API")
class NoticeController(
    private val noticeService: NoticeService,
) {
    /**
     * 공지사항 생성
     */
    @PostMapping("/notices", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "공지사항 생성 API")
    fun createNotice(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
        @RequestPart("data") noticeRequest: NoticeRequest,
    ): NoticeResponse {
        val memberId = authentication.id
        val noticeDto = noticeService.createNotice(memberId, images, noticeRequest.toRequestDto())
        return noticeDto.toResponse()
    }

    /**
     * 공지사항 전체 조회
     */
    @GetMapping("/notices")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "공지사항 전체 조회 API")
    fun getAllNotices(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestParam(defaultValue = "date") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String,
    ): NoticeListResponse {
        val memberId = authentication.id

        val sort = Sort.by(
            if (sortDirection.equals("ASC", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            },
            sortBy
        )

        val noticeListDto = noticeService.getAllNotices(memberId, sort)
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
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @PathVariable noticeId: Long,
    ): NoticeResponse {
        val memberId = authentication.id
        val noticeDto = noticeService.getDetailNotice(memberId, noticeId)
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
