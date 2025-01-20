package com.aimory.repository

import com.aimory.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {

    fun findByChildId(childId: Long): List<Photo>

    fun countByChildId(childId: Long): Int

    @Transactional
    @Modifying
    fun deleteAllByChildId(childId: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM Photo p WHERE p.id IN :photoIds")
    fun deleteByPhotoIds(photoIds: List<Long>)

    @Transactional
    @Modifying
    @Query("DELETE FROM Photo p")
    fun deleteAllPhotos()
}
