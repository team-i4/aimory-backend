package com.aimory.controller

import com.aimory.controller.dto.NoteListResponse
import com.aimory.controller.dto.NoteRequest
import com.aimory.controller.dto.NoteResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.service.NoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class NoteController(
    private val noteService: NoteService,
) {
    /**
     * 알림장 생성
     */
    @PostMapping("/notes")
    @ResponseStatus(HttpStatus.CREATED)
    fun createNote(
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val noteDto = noteService.createNote(noteRequest.toRequestDto())
        return noteDto.toResponse()
    }

    /**
     * 알림장 전체 조회
     */
    @GetMapping("/notes")
    @ResponseStatus(HttpStatus.OK)
    fun getAllNotes():
        NoteListResponse {
        val noteListDto = noteService.getAllNotes()
        val noteListResponse = noteListDto.map {
            it.toResponse()
        }
        return NoteListResponse(notes = noteListResponse)
    }

    /**
     * 알림장 단일 조회
     */
    @GetMapping("/notes/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDetailNote(
        @PathVariable noteId: Long,
    ): NoteResponse {
        val noteDto = noteService.getDetailNote(noteId)
        return noteDto.toResponse()
    }

    /**
     * 알림장 수정
     */
    @PutMapping("/notes/{noteId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateNote(
        @PathVariable noteId: Long,
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val noteDto = noteService.updateNote(noteId, noteRequest.toRequestDto())
        return noteDto.toResponse()
    }
}
