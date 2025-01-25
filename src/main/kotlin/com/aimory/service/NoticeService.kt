package com.aimory.service

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
    fun createNotice(noticeCreateRequestDto: NoticeCreateRequestDto): NoticeResponseDto {
        val notice = noticeRepository.save(noticeCreateRequestDto.toEntity())
        return notice.toResponseDto()
    }
}
