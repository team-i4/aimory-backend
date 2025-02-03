package com.aimory.exception

open class TeacherException(message: String) : RuntimeException(message)

class TeacherNotFoundException : TeacherException("존재하지 않는 선생님입니다.")

class TeacherClassroomNotFoundException : TeacherException("선생님의 반 정보가 존재하지 않습니다.")
