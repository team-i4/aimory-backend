package com.aimory.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "child")
class Child(
    name: String,
    parent: Parent,
    classroom: Classroom,
    profileImageUrl: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "profile_image_url", nullable = true)
    var profileImageUrl: String? = profileImageUrl
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    var classroom: Classroom = classroom
        protected set

    @OneToMany(mappedBy = "child", orphanRemoval = true, cascade = [CascadeType.ALL])
    var notes: MutableList<Note> = mutableListOf()
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var parent: Parent = parent
        protected set
}
