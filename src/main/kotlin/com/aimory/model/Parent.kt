package com.aimory.model

import com.aimory.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "parent")
@DiscriminatorValue("PARENT")
class Parent(
    name: String,
    email: String,
    password: String,
    children: List<Child> = emptyList(),
) : Member(
    name = name,
    email = email,
    password = password,
    role = Role.PARENT
) {
    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = [CascadeType.ALL])
    var children: MutableList<Child> = mutableListOf()
        protected set
}
