package com.aimory.repository

import com.aimory.model.PhotoChild
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PhotoChildRepository : JpaRepository<PhotoChild, Long> {
    fun findByChildId(childId: Long, sort: Sort): List<PhotoChild>

    fun findByPhotoId(photoId: Long): List<PhotoChild>

    @Modifying
    @Transactional
    @Query("DELETE FROM PhotoChild pc WHERE pc.child.id = :childId")
    fun deleteByChildId(@Param("childId") childId: Long)

    @Modifying
    @Transactional
    @Query("DELETE FROM PhotoChild pc WHERE pc.photo.id = :photoId")
    fun deleteByPhotoId(@Param("photoId") photoId: Long)
}
