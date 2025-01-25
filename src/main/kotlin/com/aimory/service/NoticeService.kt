package com.aimory.service

import com.aimory.exception.NoticeNotFoundException
import com.aimory.repository.NoticeRepository
import com.aimory.service.dto.NoticeCreateRequestDto
import com.aimory.service.dto.NoticeResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {
    /**
     * 공지사항 생성
     */
    fun createNotice(
        noticeCreateRequestDto: NoticeCreateRequestDto,
    ): NoticeResponseDto {
        val notice = noticeRepository.save(noticeCreateRequestDto.toEntity())
        return notice.toResponseDto()
    }

    /**
     * 공지사항 단일 조회
     */
    fun getDetailNotice(
        noticeId: Long,
    ): NoticeResponseDto {
        val notice = noticeRepository.findById(noticeId)
            .orElseThrow {
                NoticeNotFoundException()
            }
        return notice.toResponseDto()
    }
}
