package com.aimory.exception

open class MemberException(message: String) : RuntimeException(message)

class MemberDuplicateException : MemberException("이미 존재하는 사용자입니다.")
