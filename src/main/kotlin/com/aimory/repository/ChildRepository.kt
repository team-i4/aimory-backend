package com.aimory.repository

import com.aimory.model.Child
import org.springframework.data.jpa.repository.JpaRepository

interface ChildRepository : JpaRepository<Child, Long>
