package com.aimory.exception

open class NoticeException(
    message: String,
) : RuntimeException(message)

class NoticeNotFoundException : NoticeException("해당하는 공지사항이 없습니다.")
