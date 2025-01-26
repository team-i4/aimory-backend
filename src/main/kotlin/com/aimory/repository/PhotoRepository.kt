package com.aimory.repository

import com.aimory.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {

    fun findByChildId(childId: Long): List<Photo>

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.child.id = :childId")
    fun countByChildId(childId: Long): Int

    @Modifying
    fun deleteByChildId(childId: Long)

    @Modifying
    @Query("DELETE FROM Photo p WHERE p.id IN :photoIds")
    fun deleteByPhotoIds(photoIds: List<Long>)
}
