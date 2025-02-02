package com.aimory.exception

open class ChildException(message: String) : RuntimeException(message)

class ChildIdNotFoundException : ChildException("존재하지 않는 원아입니다.")

class ChildNotBelongToTeacherCenterException : ChildException("원아가 속한 어린이집과 선생님이 속한 어린이집이 다릅니다.")
