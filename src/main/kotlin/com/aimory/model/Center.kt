package com.aimory.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center")
class Center(
    name: String,
    director: Director,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var name: String = name
        protected set

    @OneToOne
    @JoinColumn(name = "director_id", nullable = false, unique = true)
    var director: Director = director
        protected set

    @OneToMany(mappedBy = "center", orphanRemoval = true, cascade = [CascadeType.ALL])
    var notices: MutableList<Notice> = mutableListOf()
        protected set
}
