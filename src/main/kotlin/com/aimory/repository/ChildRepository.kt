package com.aimory.repository

import com.aimory.model.Child
import org.springframework.data.jpa.repository.JpaRepository

interface ChildRepository : JpaRepository<Child, Long> {
    fun findAllByParentId(parentId: Long): List<Child>

    fun findByNameIn(names: List<String>): List<Child>
}
