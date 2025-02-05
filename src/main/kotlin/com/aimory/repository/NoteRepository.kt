package com.aimory.repository

import com.aimory.model.Note
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface NoteRepository : JpaRepository<Note, Long> {
    fun findAllByChildId(childId: Long, sort: Sort): List<Note>
    fun findAllByClassroomId(classroomId: Long, sort: Sort): List<Note>
    fun findAllByChildIdAndDate(childId: Long?, date: LocalDate?): List<Note>
}
