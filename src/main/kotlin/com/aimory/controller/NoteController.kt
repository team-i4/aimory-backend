package com.aimory.controller

import com.aimory.controller.dto.NoteRequest
import com.aimory.controller.dto.NoteResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.service.NoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
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
    @PostMapping("notes")
    @ResponseStatus(HttpStatus.CREATED)
    fun createNote(
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val noteDto = noteService.createNote(noteRequest.toRequestDto())
        return noteDto.toResponse()
    }
}
