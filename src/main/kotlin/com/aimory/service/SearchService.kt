package com.aimory.service

import com.aimory.enums.Role
import com.aimory.exception.CenterNotFoundException
import com.aimory.exception.CreateTextFailException
import com.aimory.exception.OpenAIApiRequestException
import com.aimory.exception.ParentNotFoundException
import com.aimory.exception.TeacherClassroomNotFoundException
import com.aimory.exception.TeacherNotFoundException
import com.aimory.model.Child
import com.aimory.repository.ChildRepository
import com.aimory.repository.NoteRepository
import com.aimory.repository.NoticeRepository
import com.aimory.repository.ParentRepository
import com.aimory.repository.TeacherRepository
import com.aimory.service.dto.SearchRequestDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class SearchService(
    private val noteRepository: NoteRepository,
    private val noticeRepository: NoticeRepository,
    private val childRepository: ChildRepository,
    private val teacherRepository: TeacherRepository,
    private val parentRepository: ParentRepository,
    private val webClient: WebClient,
    @Value("\${openai.api.models.chat}") private val model: String,
) {
    private val logger = LoggerFactory.getLogger(SearchService::class.java)

    /**
     * 검색 API
     */
    fun search(
        memberId: Long,
        memberRole: Role,
        searchRequestDto: SearchRequestDto,
    ): Mono<String> {
        val centerId: Long

        // 아이 파싱
        val children: List<Child> = when (memberRole) {
            Role.TEACHER -> {
                val teacher = teacherRepository.findById(memberId)
                    .orElseThrow {
                        TeacherNotFoundException()
                    }
                val teacherClassroomId = teacher.classroom?.id
                    ?: throw TeacherClassroomNotFoundException()
                centerId = teacher.centerId ?: throw CenterNotFoundException()
                childRepository.findAllByClassroomId(teacherClassroomId)
            }
            else -> {
                val parent = parentRepository.findById(memberId)
                    .orElseThrow {
                        ParentNotFoundException()
                    }
                centerId = parent.centerId ?: throw CenterNotFoundException()
                childRepository.findAllByParentId(parent.id)
            }
        }

        val parseChild = children.find {
                child ->
            searchRequestDto.content.contains(child.name)
        }

        // 날짜 파싱
        val datePattern = """(\d{4})년\s*(\d{1,2})월\s*(\d{1,2})일""".toRegex()
        val dateMatch = datePattern.find(searchRequestDto.content)
        val parseDate = dateMatch?.let {
            val (year, month, day) = it.destructured
            LocalDate.of(year.toInt(), month.toInt(), day.toInt())
        }

        // OpenAI API 요청
        val notes = noteRepository.findAllByChildIdAndDate(parseChild?.id, parseDate)
            .joinToString("\n") {
                "- ${it.content}"
            }
        val notices = noticeRepository.findAllByCenterIdAndDate(centerId, parseDate)
            .joinToString("\n") {
                "- ${it.content}"
            }

        val prompt = """
            아이 정보: ${parseChild?.name}
            게시일: $parseDate
            알림장: $notes
            공지사항: $notices

            위의 데이터를 바탕으로 다음 사용자의 질문에 답변해주세요:
            "${searchRequestDto.content}"

            답변 시 다음 지침을 따라주세요:
            1. 해당 서비스는 어린이집에서 사용하는 서비스에요.
            사용자는 선생님 혹은 아이의 학부모입니다.
            서비스의 특성에 맞게 답변해주세요.
            2. 주어진 데이터만을 사용하여 답변하세요.
            3. 정보를 단순 나열하지 말고, 문맥에 맞게 정리하여 제시하세요.
            4. 친근하고 부드러운 말투를 사용하세요.
            5. 답변은 3-4문장 정도로 간결하게 작성하세요.
            6. 질문과 직접적으로 관련 없는 정보는 언급하지 마세요.
            7. 정보가 부족하거나 없는 경우, 솔직히 모른다고 말하세요.
        """

        // 요청 데이터 설정
        val request = mapOf(
            "model" to model,
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to prompt
                )
            )
        )

        logger.info("OpenAI API Request Prompt: $prompt")

        // POST 요청 생성
        return webClient.post()
            .uri("/v1/chat/completions")
            .bodyValue(request) // body에 요청 데이터 담기
            .retrieve() // 요청 전송
            .bodyToMono(Map::class.java) // 응답 처리
            .map { response ->
                val choices = response["choices"] as? List<Map<String, Any>>
                val message = choices?.firstOrNull()?.get("message") as? Map<String, Any>
                val content = message?.get("content") as? String
                content ?: throw CreateTextFailException()
            }
            .onErrorMap { error ->
                when (error) {
                    is CreateTextFailException -> error
                    else -> OpenAIApiRequestException()
                }
            }
    }
}
