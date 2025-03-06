package com.aimory.model

import com.aimory.enums.Role
import com.aimory.exception.ClassroomTeacherDuplicateException
import com.aimory.service.dto.TeacherRequestDto
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "teacher")
@DiscriminatorValue("TEACHER")
class Teacher(
    name: String,
    email: String,
    password: String,
) : Member(
    name = name,
    email = email,
    password = password,
    role = Role.TEACHER
) {
    @Column(name = "profile_image_url", nullable = true)
    var profileImageUrl: String? = null
        protected set

    @OneToOne
    @JoinColumn(name = "classroom_id", nullable = true, unique = true)
    var classroom: Classroom? = null
        protected set

    fun update(teacherRequestDto: TeacherRequestDto) {
        this.classroom?.updateTeacher(null)
        // 변경하려는 반에 이미 담당 교사가 있을 경우 예외 처리
        teacherRequestDto.classroom?.teacher?.let {
            throw ClassroomTeacherDuplicateException()
        }
        this.classroom = teacherRequestDto.classroom
        this.profileImageUrl = teacherRequestDto.profileImageUrl
    }
}
