package com.aimory.model

import com.aimory.service.dto.NoticeRequestDto
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
@Table(name = "notice")
class Notice(
    title: String,
    content: String,
    date: LocalDate,
    center: Center,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
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
    @JoinColumn(name = "center_id", nullable = false)
    var center: Center = center
        protected set

    @OneToMany(mappedBy = "notice", orphanRemoval = true, cascade = [CascadeType.ALL])
    var noticeImages: MutableList<NoticeImage> = mutableListOf()
        protected set

    fun addImage(noticeImage: NoticeImage) {
        noticeImages.add(noticeImage)
    }

    fun update(noticeRequestDto: NoticeRequestDto) {
        this.title = noticeRequestDto.title
        this.content = noticeRequestDto.content
        this.date = noticeRequestDto.date
        this.updatedAt = LocalDate.now()
    }
}
