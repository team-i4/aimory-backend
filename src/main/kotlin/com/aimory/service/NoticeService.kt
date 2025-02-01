package com.aimory.service

import com.aimory.exception.NoticeNotFoundException
import com.aimory.repository.NoticeRepository
import com.aimory.service.dto.DeleteResponseDto
import com.aimory.service.dto.NoticeRequestDto
import com.aimory.service.dto.NoticeResponseDto
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
        noticeCreateRequestDto: NoticeRequestDto,
    ): NoticeResponseDto {
        val notice = noticeRepository.save(noticeCreateRequestDto.toEntity())
        return notice.toResponseDto()
    }

    /**
     * 공지사항 전제 조회
     */
    fun getAllNotices():
        List<NoticeResponseDto> {
        val notices = noticeRepository.findAll()
        return notices.map {
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
        noticeRequestDto: NoticeRequestDto,
    ): NoticeResponseDto {
        val notice = noticeRepository.findById(noticeId)
            .orElseThrow {
                NoticeNotFoundException()
            }
        notice.update(noticeRequestDto)
        return notice.toResponseDto()
    }

    /**
     * 공지사항 삭제
     */
    @Transactional
    fun deleteNotices(
        noticeIds: List<Long>,
    ): List<Long> {
        val deleteNoticeIds = mutableListOf<Long>()
        noticeIds.forEach {
            val notice = noticeRepository.findById(it).orElseThrow {
                NoticeNotFoundException()
            }
            noticeRepository.deleteById(notice.id)
            deleteNoticeIds.add(notice.id)
        }
        return deleteNoticeIds
    }
}
