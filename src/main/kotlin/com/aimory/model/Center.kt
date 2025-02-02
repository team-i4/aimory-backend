package com.aimory.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "center")
class Center(
    name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var name: String = name
        protected set

    @OneToMany(mappedBy = "center", orphanRemoval = true, cascade = [CascadeType.ALL])
    var notices: MutableList<Notice> = mutableListOf()
        protected set
}
