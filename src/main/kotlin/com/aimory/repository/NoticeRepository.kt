package com.aimory.repository

import com.aimory.model.Notice
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {
    fun findAllByCenterId(centerId: Long): List<Notice>
}
