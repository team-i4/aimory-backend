package com.aimory.model

import com.aimory.enums.Role
import com.aimory.exception.UnauthorizedException
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Table(name = "member")
class Member(
    centerId: Long,
    name: String,
    email: String,
    password: String,
    role: Role,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "center_id", nullable = false)
    var centerId: Long = centerId
        protected set

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "email", nullable = false, unique = true)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = role
        protected set

    fun login(credentials: String) {
        val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
        if (!passwordEncoder.matches(credentials, password)) {
            throw UnauthorizedException("비밀번호가 일치하지 않습니다.")
        }
    }
}
