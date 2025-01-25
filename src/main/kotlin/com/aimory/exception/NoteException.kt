package com.aimory.exception

open class NoteException(message: String) : RuntimeException(message)

class NoteNotFoundException : NoteException("해당하는 알림장이 없습니다.")
