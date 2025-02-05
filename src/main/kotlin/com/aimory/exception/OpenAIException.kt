package com.aimory.exception

open class OpenAIException(message: String) : RuntimeException(message)

class CreateImageFailException : OpenAIException("이미지 생성에 실패했습니다.")
class CreateTextFailException : OpenAIException("텍스트 생성에 실패했습니다.")
class OpenAIApiRequestException : OpenAIException("OpenAI API 요청에 실패했습니다.")
