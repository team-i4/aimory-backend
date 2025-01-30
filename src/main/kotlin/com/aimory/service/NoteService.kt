package com.aimory.service

import com.aimory.exception.CreateImageFailException
import com.aimory.exception.NoteNotFoundException
import com.aimory.exception.OpenAIApiRequestException
import com.aimory.repository.NoteRepository
import com.aimory.service.dto.DeleteResponseDto
import com.aimory.service.dto.NoteImageRequestDto
import com.aimory.service.dto.NoteRequestDto
import com.aimory.service.dto.NoteResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true)
class NoteService(
    private val noteRepository: NoteRepository,
    private val webClient: WebClient,
    @Value("\${openai.model}") private val model: String,
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

    /**
     * 알림장 수정
     */
    @Transactional
    fun updateNote(
        noteId: Long,
        noteRequestDto: NoteRequestDto,
    ): NoteResponseDto {
        val note = noteRepository.findById(noteId)
            .orElseThrow {
                NoteNotFoundException()
            }
        note.update(noteRequestDto)
        return note.toResponseDto()
    }

    /**
     * 알림장 삭제
     */
    @Transactional
    fun deleteNotes(
        noteIdList: List<Long>,
    ): DeleteResponseDto {
        noteIdList.forEach {
            val note = noteRepository.findById(it).orElseThrow {
                NoteNotFoundException()
            }
            noteRepository.deleteById(note.id)
        }
        return DeleteResponseDto("성공적으로 삭제되었습니다.")
    }

    /**
     * 알림장 그림 생성
     */
    fun createNoteImage(
        noteImageRequestDto: NoteImageRequestDto,
    ): Mono<String> {
        // 프롬프트 정의
        val prompt =
            """
                전달되는 텍스트 중 핵심 내용 한가지를 찾아 해당 상황에 대한 그림을 일러스트풍으로 그려 주세요. : ${noteImageRequestDto.content}
            """

        // 요청 데이터 설정
        val request = mapOf(
            "model" to model,
            "prompt" to prompt,
            "n" to 1,
            "size" to "1024x1024"
        )

        // POST 요청 생성
        return webClient.post()
            .bodyValue(request) // body에 요청 데이터 담기
            .retrieve() // 요청 전송
            .bodyToMono(Map::class.java) // 응답 처리
            .map { response ->
                val dataList = response["data"] as? List<Map<String, Any>>
                val imageUrl = dataList?.getOrNull(0)?.get("url") as? String
                imageUrl ?: throw CreateImageFailException()
            }
            .onErrorMap { error ->
                when (error) {
                    is CreateImageFailException -> error
                    else -> OpenAIApiRequestException()
                }
            }
    }
}
