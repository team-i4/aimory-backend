package com.aimory.model

import com.aimory.enums.PhotoStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "photo")
class Photo(
    imageUrl: String,
    status: PhotoStatus = PhotoStatus.PENDING,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = imageUrl
        protected set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @OneToMany(mappedBy = "photo", cascade = [CascadeType.ALL], orphanRemoval = true)
    var photoChildren: MutableList<PhotoChild> = mutableListOf()
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: PhotoStatus = status
        protected set

    fun addChild(child: Child) {
        if (photoChildren.none { it.child == child }) {
            photoChildren.add(PhotoChild(photo = this, child = child))
        }
    }

    fun changeStatus(newStatus: PhotoStatus) {
        status = newStatus
    }
}
