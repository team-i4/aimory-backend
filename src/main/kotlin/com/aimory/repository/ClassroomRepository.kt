package com.aimory.repository

import com.aimory.model.Classroom
import org.springframework.data.jpa.repository.JpaRepository

interface ClassroomRepository : JpaRepository<Classroom, Long>
