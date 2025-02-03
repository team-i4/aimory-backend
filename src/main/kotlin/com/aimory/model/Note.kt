package com.aimory.model

import com.aimory.service.dto.NoteRequestDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "note")
class Note(
    content: String,
    date: LocalDate,
    child: Child,
    classroom: Classroom,
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    var child: Child = child
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    var classroom: Classroom = classroom
        protected set

    @OneToMany(mappedBy = "note", orphanRemoval = true, cascade = [CascadeType.ALL])
    var noteImages: MutableList<NoteImage> = mutableListOf()
        protected set

    fun addImage(noteImage: NoteImage) {
        noteImages.add(noteImage)
    }

    fun update(child: Child, noteRequestDto: NoteRequestDto) {
        this.child = child
        this.content = noteRequestDto.content
        this.date = noteRequestDto.date
        this.updatedAt = LocalDate.now()
    }
}
