package com.aimory.exception

open class ParentException(message: String) : RuntimeException(message)

class ParentNotFoundException : ParentException("존재하지 않는 부모입니다.")
