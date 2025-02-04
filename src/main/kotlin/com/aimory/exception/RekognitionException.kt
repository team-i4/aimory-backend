package com.aimory.exception

open class RekognitionException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

class DetectFacesFailException(message: String, cause: Throwable? = null) :
    RekognitionException(message, cause)
class CompareFacesFailException(message: String, cause: Throwable? = null) :
    RekognitionException(message, cause)
