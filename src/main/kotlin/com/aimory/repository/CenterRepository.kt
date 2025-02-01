package com.aimory.repository

import com.aimory.model.Center
import org.springframework.data.jpa.repository.JpaRepository

interface CenterRepository : JpaRepository<Center, Long>
