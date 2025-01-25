package com.aimory.service

import com.aimory.exception.NoticeNotFoundException
import com.aimory.repository.NoticeRepository
import com.aimory.service.dto.NoticeCreateRequestDto
import com.aimory.service.dto.NoticeResponseDto
import com.aimory.service.dto.NoticeUpdateRequestDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {
    /**
     * 공지사항 생성
     */
    @Transactional
    fun createNotice(
        noticeCreateRequestDto: NoticeCreateRequestDto,
    ): NoticeResponseDto {
        val notice = noticeRepository.save(noticeCreateRequestDto.toEntity())
        return notice.toResponseDto()
    }

    /**
     * 공지사항 전제 조회
     */
    fun getAllNotices():
        List<NoticeResponseDto> {
        val noticeList = noticeRepository.findAll()
        return noticeList.map {
            it.toResponseDto()
        }
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

    /**
     * 공지사항 수정
     */
    @Transactional
    fun updateNotice(
        noticeId: Long,
        noticeUpdateRequestDto: NoticeUpdateRequestDto,
    ): NoticeResponseDto {
        val notice = noticeRepository.findById(noticeId)
            .orElseThrow {
                NoticeNotFoundException()
            }
        notice.update(noticeUpdateRequestDto)
        return notice.toResponseDto()
    }
}
