package com.aimory.exception

open class PhotoException(message: String) : RuntimeException(message)

class PhotoNotFoundException(photoId: Long) : PhotoException("해당 사진($photoId)이 찾을 수 없습니다.")
class ChildNotFoundException(childId: Long) : PhotoException("해당 원아($childId)를 찾을 수 없습니다.")
class InvalidPhotoUploadException : PhotoException("업로드할 파일이 없습니다.")
class InvalidChildIdException : PhotoException("올바른 원아 ID를 입력하세요.")
class EmptyPhotoIdListException : PhotoException("삭제할 사진 ID 목록이 비어 있습니다.")
class EmptyChildIdListException : PhotoException("삭제할 원아 ID 목록이 비어 있습니다.")
