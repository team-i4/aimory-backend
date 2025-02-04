package com.aimory.service

import com.aimory.enums.Role
import com.aimory.exception.ChildIdNotFoundException
import com.aimory.exception.ChildNotBelongToParentException
import com.aimory.exception.ChildNotBelongToTeacherClassroomException
import com.aimory.exception.CreateImageFailException
import com.aimory.exception.NoteNotFoundException
import com.aimory.exception.OpenAIApiRequestException
import com.aimory.exception.ParentNotFoundException
import com.aimory.exception.TeacherClassroomNotFoundException
import com.aimory.exception.TeacherNotFoundException
import com.aimory.model.Child
import com.aimory.model.Member
import com.aimory.model.Note
import com.aimory.model.NoteImage
import com.aimory.model.Parent
import com.aimory.model.Teacher
import com.aimory.repository.ChildRepository
import com.aimory.repository.NoteRepository
import com.aimory.repository.ParentRepository
import com.aimory.repository.TeacherRepository
import com.aimory.service.dto.NoteImageRequestDto
import com.aimory.service.dto.NoteRequestDto
import com.aimory.service.dto.NoteResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.UUID

@Service
@Transactional(readOnly = true)
class NoteService(
    private val noteRepository: NoteRepository,
    private val childRepository: ChildRepository,
    private val teacherRepository: TeacherRepository,
    private val parentRepository: ParentRepository,
    private val webClient: WebClient,
    private val s3Service: S3Service,
    @Value("\${openai.api.models.image}") private val model: String,
) {
    /**
     * 알림장 생성
     */
    @Transactional
    fun createNote(
        memberId: Long,
        noteRequestDto: NoteRequestDto,
    ): NoteResponseDto {
        val teacher = checkTeacherExists(memberId)
        val child = checkChildExists(noteRequestDto.childId)
        checkChildBelongToTeacherClassroom(child, teacher)
        val note = noteRepository.save(noteRequestDto.toEntity(child))

        if (noteRequestDto.image != null) {
            // OpenAI에서 생성된 이미지 다운로드
            val imageBytes = URL(noteRequestDto.image).openStream().use {
                it.readBytes()
            }

            // ByteArray를 MultipartFile로 변환
            val multipartFile = MockMultipartFile(
                UUID.randomUUID().toString(), // 파라미터 이름
                UUID.randomUUID().toString() + ".jpg", // 파일 이름
                "image/jpg", // 컨텐츠 타입
                ByteArrayInputStream(imageBytes) // 파일 내용
            )

            // S3에 이미지 올리기
            val imageUrl = s3Service.uploadFile(multipartFile)
            val noteImage = NoteImage(
                imageUrl = imageUrl,
                note = note
            )
            note.addImage(noteImage)
        }

        return note.toResponseDto()
    }

    /**
     * 알림장 전체 조회
     */
    fun getAllNotes(
        memberId: Long,
        memberRole: Role,
        sort: Sort,
    ): List<NoteResponseDto> {
        val teacher = checkTeacherExists(memberId)
        val teacherClassroomId = teacher.classroom?.id
            ?: throw TeacherClassroomNotFoundException()
        val notes = when (memberRole) {
            Role.TEACHER -> {
                noteRepository.findAllByClassroomId(teacherClassroomId, sort)
            }
            else -> {
                val parent = checkParentExists(memberId)
                val children = childRepository.findAllByParentId(parent.id)
                children.filter {
                    it.classroom.id == teacherClassroomId
                }.flatMap { child ->
                    noteRepository.findAllByChildId(child.id, sort)
                }
            }
        }
        return notes.map {
            it.toResponseDto()
        }
    }

    /**
     * 알림장 단일 조회
     */
    fun getDetailNote(
        memberId: Long,
        memberRole: Role,
        noteId: Long,
    ): NoteResponseDto {
        val note = checkNoteExists(noteId)
        when (memberRole) {
            Role.TEACHER -> {
                val teacher = checkTeacherExists(memberId)
                checkChildBelongToTeacherClassroom(note.child, teacher)
            }
            else -> {
                val parent = checkParentExists(memberId)
                checkChildBelongToParent(note.child, parent)
            }
        }
        return note.toResponseDto()
    }

    /**
     * 알림장 수정
     */
    @Transactional
    fun updateNote(
        memberId: Long,
        noteId: Long,
        noteRequestDto: NoteRequestDto,
    ): NoteResponseDto {
        val teacher = checkTeacherExists(memberId)
        val child = checkChildExists(noteRequestDto.childId)
        val note = checkNoteExists(noteId)
        checkChildBelongToTeacherClassroom(child, teacher)
        note.update(child, noteRequestDto)
        return note.toResponseDto()
    }

    /**
     * 알림장 삭제
     */
    @Transactional
    fun deleteNotes(
        memberId: Long,
        noteIdList: List<Long>,
    ): List<Long> {
        val teacher = checkTeacherExists(memberId)
        val deleteNoteIds = mutableListOf<Long>()
        noteIdList.forEach {
            val note = checkNoteExists(it)
            checkChildBelongToTeacherClassroom(note.child, teacher)

            // S3에 저장되어 있는 이미지 삭제
            val imageUrls = note.noteImages.map {
                it.imageUrl
            }
            s3Service.deleteFiles(imageUrls)

            noteRepository.deleteById(note.id)
            deleteNoteIds.add(note.id)
        }
        return deleteNoteIds
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
                전달되는 텍스트 중 핵심 내용 한가지를 찾아 해당 상황에 대한 그림을 일러스트풍으로 그려 주세요.: ${noteImageRequestDto.content}
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
            .uri("/v1/images/generations")
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

    /**
     * 원아 존재 여부 확인
     */
    private fun checkChildExists(childId: Long): Child {
        val child = childRepository.findById(childId)
            .orElseThrow {
                // todo: 일관된 예외 정의 필요 (ChildNotFoundException)
                ChildIdNotFoundException()
            }
        return child
    }

    /**
     * 선생님 존재 여부 확인
     */
    private fun checkTeacherExists(memberId: Long): Teacher {
        return teacherRepository.findById(memberId)
            .orElseThrow {
                TeacherNotFoundException()
            }
    }

    /**
     * 부모 존재 여부 확인
     */
    private fun checkParentExists(memberId: Long): Parent {
        return parentRepository.findById(memberId)
            .orElseThrow {
                ParentNotFoundException()
            }
    }

    /**
     * 알림장 존재 여부 확인
     */
    private fun checkNoteExists(noteId: Long): Note {
        return noteRepository.findById(noteId)
            .orElseThrow {
                NoteNotFoundException()
            }
    }

    /**
     * 요청된 원아의 반과 선생님의 반 일치 여부 확인
     */
    private fun checkChildBelongToTeacherClassroom(child: Child, teacher: Teacher) {
        val teacherClassroomId = teacher.classroom?.id
            ?: throw TeacherClassroomNotFoundException()
        if (child.classroom.id != teacherClassroomId) {
            throw ChildNotBelongToTeacherClassroomException()
        }
    }

    /**
     * 요청된 원아가 부모의 자녀인지 확인
     */
    private fun checkChildBelongToParent(child: Child, parent: Member) {
        if (child.parent.id != parent.id) {
            throw ChildNotBelongToParentException()
        }
    }
}
