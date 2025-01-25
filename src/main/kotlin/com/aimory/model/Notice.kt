package com.aimory.model

import com.aimory.service.dto.NoticeUpdateRequestDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "notice")
class Notice(
    title: String,
    content: String,
    date: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

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

    @OneToMany(mappedBy = "notice", orphanRemoval = true, cascade = [CascadeType.ALL])
    var noticeImages: MutableList<NoticeImage> = mutableListOf()
        protected set

    fun update(noticeUpdateRequestDto: NoticeUpdateRequestDto) {
        this.title = noticeUpdateRequestDto.title
        this.content = noticeUpdateRequestDto.content
        this.date = noticeUpdateRequestDto.date
        this.updatedAt = LocalDate.now()
    }
}
