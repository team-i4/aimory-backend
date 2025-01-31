package com.aimory.repository

import com.aimory.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {

    fun findByChildId(childId: Long): List<Photo>

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.child.id = :childId")
    fun countByChildId(childId: Long): Int

    @Query("SELECT p FROM Photo p WHERE p.child.id IN :childIds")
    fun findAllByChildIds(childIds: List<Long>): List<Photo>
}
