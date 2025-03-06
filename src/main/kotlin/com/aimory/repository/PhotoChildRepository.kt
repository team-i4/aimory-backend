package com.aimory.repository

import com.aimory.model.PhotoChild
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoChildRepository : JpaRepository<PhotoChild, Long> {
    fun findByChildId(childId: Long, sort: Sort): List<PhotoChild>

    fun findByPhotoId(photoId: Long): List<PhotoChild>

    fun deleteByChildId(childId: Long)

    fun deleteByPhotoId(photoId: Long)
}
