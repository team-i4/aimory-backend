package com.aimory.exception

open class CenterException(message: String) : RuntimeException(message)

class CenterNotFoundException : CenterException("존재하지 않는 어린이집입니다.")
