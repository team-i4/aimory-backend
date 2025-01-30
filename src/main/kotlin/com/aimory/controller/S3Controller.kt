package com.aimory.controller

import com.aimory.controller.dto.S3DeleteRequest
import com.aimory.controller.dto.S3DeleteResponse
import com.aimory.controller.dto.S3UploadResponse
import com.aimory.exception.S3Exception
import com.aimory.service.S3Service
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class S3Controller(
    private val s3Service: S3Service,
) {

    @PostMapping("/s3/files", consumes = ["multipart/form-data"])
    @Operation(summary = "파일 업로드", description = "S3에 파일을 업로드하고 URL을 반환합니다.")
    fun uploadFile(@RequestParam("file") file: MultipartFile): S3UploadResponse {
        if (file.isEmpty) {
            throw S3Exception(S3Exception.INVALID_FILE_UPLOAD)
        }

        val fileUrl = s3Service.uploadFile(file)
        return S3UploadResponse(fileUrl)
    }

    @DeleteMapping("/s3/files")
    @Operation(summary = "파일 삭제", description = "S3에서 지정된 파일을 삭제합니다.")
    fun deleteFiles(@RequestBody deleteRequest: S3DeleteRequest): S3DeleteResponse {
        val deletedFiles = s3Service.deleteFiles(deleteRequest.fileUrls)
        return S3DeleteResponse(deletedFiles)
    }
}
