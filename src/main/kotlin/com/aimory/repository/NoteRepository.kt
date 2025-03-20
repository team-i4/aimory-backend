package com.aimory.repository

import com.aimory.model.Note
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<Note, Long> {
    fun findByChild_IdAndContentContaining(
        childId: Long,
        Keyword: String?,
        sort: Sort,
    ): List<Note>

    fun findAllByChild_Id(childId: Long, sort: Sort): List<Note>

    fun findByClassroom_IdAndContentContaining(
        classroomId: Long,
        keyword: String?,
        sort: Sort,
    ): List<Note>

    fun findAllByClassroom_Id(classroomId: Long, sort: Sort): List<Note>
}
