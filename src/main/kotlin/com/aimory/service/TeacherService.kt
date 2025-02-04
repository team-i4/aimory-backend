package com.aimory.service

import com.aimory.exception.ClassroomNotFoundException
import com.aimory.exception.MemberNotFoundException
import com.aimory.repository.ClassroomRepository
import com.aimory.repository.TeacherRepository
import com.aimory.service.dto.TeacherRequestDto
import com.aimory.service.dto.TeacherResponseDto
import com.aimory.service.dto.toResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class TeacherService(
    private val teacherRepository: TeacherRepository,
    private val classroomRepository: ClassroomRepository,
    private val s3Service: S3Service,
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

    /**
     * 선생님 정보(프로필 이미지, 담당반) 수정
     */
    @Transactional
    fun updateTeacher(teacherId: Long, classroomId: Long?, image: MultipartFile): TeacherResponseDto {
        val classroom = classroomId?.let {
            classroomRepository.findById(classroomId)
                .orElseThrow { ClassroomNotFoundException() }
        }
        val teacher = teacherRepository.findById(teacherId)
            .orElseThrow { MemberNotFoundException() }

        val profileImageUrl = s3Service.uploadFile(image)
        teacher.update(TeacherRequestDto(profileImageUrl, classroom))
        return teacher.toResponseDto()
    }
}
