package com.aimory.service

import com.aimory.controller.dto.JoinResponse
import com.aimory.controller.dto.toJoinResponse
import com.aimory.exception.MemberDuplicateException
import com.aimory.exception.MemberNotFoundException
import com.aimory.model.Member
import com.aimory.repository.MemberRepository
import com.aimory.service.dto.JoinRequestDto
import com.aimory.service.dto.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun join(joinRequestDto: JoinRequestDto): JoinResponse {
        memberRepository.findByEmail(joinRequestDto.email)?.let {
            throw MemberDuplicateException()
        }
        val member: Member = memberRepository.save(joinRequestDto.toEntity())
        return member.toJoinResponse()
    }

    @Transactional
    fun login(email: String, password: String): Member {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException()
        member.login(password)
        return member
    }
}
