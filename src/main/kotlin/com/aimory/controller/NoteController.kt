package com.aimory.controller

import com.aimory.controller.dto.DeleteRequest
import com.aimory.controller.dto.DeleteResponse
import com.aimory.controller.dto.NoteImageRequest
import com.aimory.controller.dto.NoteImageResponse
import com.aimory.controller.dto.NoteListResponse
import com.aimory.controller.dto.NoteRequest
import com.aimory.controller.dto.NoteResponse
import com.aimory.controller.dto.toRequestDto
import com.aimory.controller.dto.toResponse
import com.aimory.security.JwtAuthentication
import com.aimory.service.NoteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val memberId = authentication.id
        val noteDto = noteService.createNote(memberId, noteRequest.toRequestDto())
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
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @PathVariable noteId: Long,
        @RequestBody noteRequest: NoteRequest,
    ): NoteResponse {
        val memberId = authentication.id
        val noteDto = noteService.updateNote(memberId, noteId, noteRequest.toRequestDto())
        return noteDto.toResponse()
    }

    /**
     * 알림장 삭제
     */
    @DeleteMapping("/notes")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "알림장 삭제 API")
    fun deleteNotes(
        @RequestBody deleteRequest: DeleteRequest,
    ): DeleteResponse {
        val deleteNoteIds = noteService.deleteNotes(deleteRequest.data)
        return DeleteResponse(deleteNoteIds)
    }

    /**
     * 알림장 그림 생성
     */
    @PostMapping("/note-images")
    fun generateNoteImage(
        @RequestBody noteImageRequest: NoteImageRequest,
    ): Mono<NoteImageResponse> {
        val imageUrl = noteService.createNoteImage(noteImageRequest.toRequestDto())
        return imageUrl.map {
                it ->
            NoteImageResponse(it)
        }
    }
}
