package com.aimory.repository

import com.aimory.model.Notice
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface NoticeRepository : JpaRepository<Notice, Long> {
    fun findAllByCenterId(centerId: Long, sort: Sort): List<Notice>
    fun findAllByCenterIdAndDate(centerId: Long, date: LocalDate?): List<Notice>
}
