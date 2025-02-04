package com.aimory.exception

import com.amazonaws.services.s3.model.AmazonS3Exception

open class S3Exception(message: String) : AmazonS3Exception(message) {
    companion object {
        const val FILE_TOO_LARGE = "파일 크기가 너무 큽니다. 10MB 이하의 파일만 업로드 가능합니다."
        const val FILE_UPLOAD_ERROR = "파일 업로드 중 오류가 발생했습니다."
        const val FILE_DELETE_ERROR = "파일 삭제 중 오류가 발생했습니다."
        const val INVALID_FILE_NAME = "파일 이름이 유효하지 않습니다."
    }
}
