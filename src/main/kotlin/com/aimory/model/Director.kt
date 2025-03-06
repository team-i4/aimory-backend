package com.aimory.model

import com.aimory.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "director")
@DiscriminatorValue("DIRECTOR")
class Director(
    name: String,
    email: String,
    password: String,
) : Member(
    name = name,
    email = email,
    password = password,
    role = Role.DIRECTOR
) {
    @Column(name = "profile_image_url", nullable = true)
    var profileImageUrl: String? = null
        protected set

    @OneToOne(mappedBy = "director")
    var center: Center? = null
        protected set
}
