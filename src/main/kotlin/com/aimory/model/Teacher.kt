package com.aimory.model

import com.aimory.enums.Role
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
    centerId: Long,
    name: String,
    email: String,
    password: String,
) : Member(
    centerId,
    name,
    email,
    password,
    role = Role.TEACHER
) {
    @Column(name = "profile_image_url", nullable = true)
    var profileImageUrl: String? = null
        protected set

    @OneToOne
    @JoinColumn(name = "classroom_id", nullable = true, unique = true)
    var classroom: Classroom? = null
        protected set
}
