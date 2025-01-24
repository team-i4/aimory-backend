package com.aimory.model

import jakarta.persistence.*

@Entity
@Table(name = "photo")
class Photo(
    imageUrl: String,
    child: Child
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = imageUrl
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    var child: Child = child
        protected set
}

