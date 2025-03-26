package com.aimory.repository

import com.aimory.model.Notice
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {
    fun findByCenterIdAndTitleContainingOrContentContaining(
        classroomId: Long,
        titleKeyword: String?,
        contentKeyword: String?,
        sort: Sort,
    ): List<Notice>

    fun findAllByCenterId(centerId: Long, sort: Sort): List<Notice>
}
