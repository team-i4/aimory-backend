package com.aimory.model

import jakarta.persistence.*

@Entity
@Table(name = "photo")
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    var child: Child
) {
    constructor(imageUrl: String, child: Child) : this(0, imageUrl, child)
}
