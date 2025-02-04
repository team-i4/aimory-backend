package com.aimory.repository

import com.aimory.enums.PhotoStatus
import com.aimory.model.Photo
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {

    fun findByChildren_Id(childId: Long, sort: Sort): List<Photo>

    @Query("SELECT p FROM Photo p JOIN p.children c WHERE c.id IN :childIds")
    fun findAllByChildIds(childIds: List<Long>): List<Photo>

    fun findAllByStatus(status: PhotoStatus, sort: Sort): List<Photo>

    fun findByStatus(status: PhotoStatus): List<Photo>
}
