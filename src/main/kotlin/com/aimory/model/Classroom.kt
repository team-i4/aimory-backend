package com.aimory.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "classroom")
class Classroom(
    name: String,
    teacher: Teacher?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @OneToOne(mappedBy = "classroom")
    var teacher: Teacher? = teacher
        protected set
}
