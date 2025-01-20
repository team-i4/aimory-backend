package com.aimory.model

import com.aimory.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "teacher")
@DiscriminatorValue("TEACHER")
class Teacher(
    name: String,
    email: String,
    password: String,
) : Member(
    name,
    email,
    password,
    role = Role.PARENT
) {
    @Column(name = "profile_image_url", nullable = false)
    var profileImageUrl: String? = null
        protected set
}
