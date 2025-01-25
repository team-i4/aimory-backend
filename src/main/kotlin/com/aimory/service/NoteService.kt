package com.aimory.service

import com.aimory.exception.NoteNotFoundException
import com.aimory.repository.NoteRepository
import com.aimory.service.dto.NoteRequestDto
import com.aimory.service.dto.NoteResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NoteService(
    private val noteRepository: NoteRepository,
) {
    /**
     * 알림장 생성
     */
    @Transactional
    fun createNote(
        noteRequestDto: NoteRequestDto,
    ): NoteResponseDto {
        val note = noteRepository.save(noteRequestDto.toEntity())
        return note.toResponseDto()
    }

    /**
     * 알림장 전체 조회
     */
    fun getAllNotes():
        List<NoteResponseDto> {
        val noteList = noteRepository.findAll()
        return noteList.map {
            it.toResponseDto()
        }
    }

    /**
     * 알림장 단일 조회
     */
    fun getDetailNote(
        noteId: Long,
    ): NoteResponseDto {
        val note = noteRepository.findById(noteId)
            .orElseThrow {
                NoteNotFoundException()
            }
        return note.toResponseDto()
    }
}
