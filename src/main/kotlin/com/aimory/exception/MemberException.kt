package com.aimory.exception

open class MemberException(message: String) : RuntimeException(message)

class MemberDuplicateException : MemberException("이미 존재하는 사용자입니다.")

class MemberNotFoundException : MemberException("존재하지 않는 사용자입니다.")

class MemberNotBelongToCenterException : MemberException("해당 회원은 해당 게시물의 어린이집에 속해있지 않습니다.")

class MemberCannotAccessPhotoException : MemberException("해당 회원은 해당 사진에 대한 접근 권한이 없습니다.")

class MemberCannotAccessChildException : MemberException("해당 회원은 해당 원아에 대한 접근 권한이 없습니다.")
