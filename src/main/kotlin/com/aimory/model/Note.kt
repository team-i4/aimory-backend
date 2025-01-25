package com.aimory.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "note")
class Note(
    content: String,
    date: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @Column(name = "date", nullable = false)
    var date: LocalDate = date
        protected set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now()
        protected set

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDate = createdAt
        protected set
}
