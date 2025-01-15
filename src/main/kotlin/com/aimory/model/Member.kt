package com.aimory.model

import com.aimory.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
    name: String,
    email: String,
    password: String,
    role: Role,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var name: String = name
        protected set

    var email: String = email
        protected set

    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set
}
