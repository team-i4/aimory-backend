package com.aimory.repository

import com.aimory.model.NoticeImage
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeImageRepository : JpaRepository<NoticeImage, Long>
