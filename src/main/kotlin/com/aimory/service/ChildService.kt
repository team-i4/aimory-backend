package com.aimory.service

import com.aimory.controller.dto.ChildResponse
import com.aimory.controller.dto.toChildResponse
import com.aimory.exception.MemberNotFoundException
import com.aimory.repository.ChildRepository
import com.aimory.repository.ParentRepository
import com.aimory.service.dto.ChildRequestDto
import com.aimory.service.dto.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChildService(
    private val childRepository: ChildRepository,
    private val parentRepository: ParentRepository,
) {
    /**
     * 자녀 정보 생성
     */
    @Transactional
    fun createChild(
        childRequestDto: ChildRequestDto,
    ): ChildResponse {
        val parent = parentRepository.findById(childRequestDto.parentId)
            .orElseThrow {
                MemberNotFoundException()
            }
        val child = childRepository.save(childRequestDto.toEntity(parent))
        return child.toChildResponse()
    }
}
