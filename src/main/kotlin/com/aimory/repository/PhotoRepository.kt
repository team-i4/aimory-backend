package com.aimory.repository

import com.aimory.enums.PhotoStatus
import com.aimory.model.Photo
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {
    fun findAllByStatus(status: PhotoStatus, sort: Sort): List<Photo>

    fun findByStatus(status: PhotoStatus): List<Photo>
}
