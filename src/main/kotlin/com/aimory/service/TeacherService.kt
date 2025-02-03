package com.aimory.service

import com.aimory.exception.MemberNotFoundException
import com.aimory.repository.TeacherRepository
import com.aimory.service.dto.TeacherResponseDto
import com.aimory.service.dto.toResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TeacherService(
    private val teacherRepository: TeacherRepository,
) {
    /**
     * 선생님 정보 조회
     */
    fun getDetailTeacher(
        teacherId: Long,
    ): TeacherResponseDto {
        val teacher = teacherRepository.findById(teacherId)
            .orElseThrow {
                MemberNotFoundException()
            }
        return teacher.toResponseDto()
    }
}
