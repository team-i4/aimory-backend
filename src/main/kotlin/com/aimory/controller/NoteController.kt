package com.aimory.controller

import com.aimory.controller.dto.NoteDeleteRequest
import com.aimory.controller.dto.NoteListResponse
import com.aimory.controller.dto.NoteRequest
import com.aimory.controller.dto.NoteResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.service.NoteService
import com.aimory.service.dto.DeleteResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "notes", description = "알림장 API")
class NoteController(
    private val noteService: NoteService,
) {
    /**
     * 알림장 생성
     */
    @PostMapping("/notes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "알림장 생성 API")
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
    @Operation(summary = "알림장 전체 조회 API")
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
    @Operation(summary = "알림장 단일 조회 API")
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
    @Operation(summary = "알림장 수정 API")
    fun updateNote(
        @PathVariable noteId: Long,
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val noteDto = noteService.updateNote(noteId, noteRequest.toRequestDto())
        return noteDto.toResponse()
    }

    /**
     * 알림장 삭제
     */
    @DeleteMapping("/notes")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "알림장 삭제 API")
    fun deleteNotes(
        @RequestBody noteDeleteRequest: NoteDeleteRequest,
    ): DeleteResponseDto {
        return noteService.deleteNotes(noteDeleteRequest.data)
    }
}
