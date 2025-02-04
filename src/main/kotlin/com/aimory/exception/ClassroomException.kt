package com.aimory.exception

open class ClassroomException(message: String) : RuntimeException(message)

class ClassroomNotFoundException : ClassroomException("반 정보가 존재하지 않습니다.")

class ClassroomTeacherDuplicateException : ClassroomException("이미 담당 교사가 존재하는 반입니다.")
