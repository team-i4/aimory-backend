package com.aimory.service

import com.aimory.exception.CenterNotFoundException
import com.aimory.exception.MemberNotBelongToCenterException
import com.aimory.exception.MemberNotFoundException
import com.aimory.exception.NoticeNotFoundException
import com.aimory.model.Member
import com.aimory.model.Notice
import com.aimory.model.NoticeImage
import com.aimory.repository.CenterRepository
import com.aimory.repository.MemberRepository
import com.aimory.repository.NoticeRepository
import com.aimory.service.dto.NoticeRequestDto
import com.aimory.service.dto.NoticeResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val memberRepository: MemberRepository,
    private val centerRepository: CenterRepository,
    private val s3Service: S3Service,
) {
    /**
     * 공지사항 생성
     */
    @Transactional
    fun createNotice(
        memberId: Long,
        images: List<MultipartFile>?,
        noticeRequestDto: NoticeRequestDto,
    ): NoticeResponseDto {
        val member = checkMemberExists(memberId)
        val center = centerRepository.findById(member.centerId)
            .orElseThrow {
                CenterNotFoundException()
            }
        val notice = noticeRepository.save(noticeRequestDto.toEntity(center))

        // S3에 이미지 올리기
        images?.forEach {
            val imageUrl = s3Service.uploadFile(it)
            val noticeImage = NoticeImage(
                imageUrl = imageUrl,
                notice = notice
            )
            notice.addImage(noticeImage)
        }

        return notice.toResponseDto()
    }

    /**
     * 공지사항 전제 조회
     */
    fun getAllNotices(
        memberId: Long,
        sort: Sort,
    ): List<NoticeResponseDto> {
        val member = checkMemberExists(memberId)
        val notices = noticeRepository.findAllByCenterId(member.centerId, sort)
        return notices.map {
            it.toResponseDto()
        }
    }

    /**
     * 공지사항 단일 조회
     */
    fun getDetailNotice(
        memberId: Long,
        noticeId: Long,
    ): NoticeResponseDto {
        val member = checkMemberExists(memberId)
        val notice = checkNoticeExists(noticeId)
        checkMemberBelongsToCenter(member, notice)
        return notice.toResponseDto()
    }

    /**
     * 공지사항 수정
     */
    @Transactional
    fun updateNotice(
        memberId: Long,
        noticeId: Long,
        noticeRequestDto: NoticeRequestDto,
    ): NoticeResponseDto {
        val member = checkMemberExists(memberId)
        val notice = checkNoticeExists(noticeId)
        checkMemberBelongsToCenter(member, notice)
        notice.update(noticeRequestDto)
        return notice.toResponseDto()
    }

    /**
     * 공지사항 삭제
     */
    @Transactional
    fun deleteNotices(
        memberId: Long,
        noticeIds: List<Long>,
    ): List<Long> {
        val member = checkMemberExists(memberId)
        val deleteNoticeIds = mutableListOf<Long>()
        noticeIds.forEach {
            val notice = checkNoticeExists(it)
            checkMemberBelongsToCenter(member, notice)

            // S3에 저장되어 있는 이미지 삭제
            val imageUrls = notice.noticeImages.map {
                it.imageUrl
            }
            s3Service.deleteFiles(imageUrls)

            noticeRepository.deleteById(notice.id)
            deleteNoticeIds.add(notice.id)
        }
        return deleteNoticeIds
    }

    /**
     * 사용자 존재 여부 확인
     */
    private fun checkMemberExists(memberId: Long): Member {
        val member = memberRepository.findById(memberId)
            .orElseThrow {
                MemberNotFoundException()
            }
        return member
    }

    /**
     * 공지사항 존재 여부 확인
     */
    private fun checkNoticeExists(noticeId: Long): Notice {
        val notice = noticeRepository.findById(noticeId)
            .orElseThrow {
                NoticeNotFoundException()
            }
        return notice
    }

    /**
     * 공지사항의 어린이집과 사용자의 어린이집 일치 여부 확인
     */
    private fun checkMemberBelongsToCenter(member: Member, notice: Notice) {
        if (member.centerId != notice.center.id) {
            throw MemberNotBelongToCenterException()
        }
    }
}
