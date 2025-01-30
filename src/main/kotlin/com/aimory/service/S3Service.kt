package com.aimory.service

import com.aimory.exception.S3Exception
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class S3Service(
    private val amazonS3: AmazonS3,
) {

    @Value("\${cloud.aws.s3.bucketName}")
    private lateinit var bucketName: String

    fun uploadFile(file: MultipartFile): String {
        val originalFilename = file.originalFilename?.takeIf { it.isNotBlank() }
            ?: throw S3Exception(S3Exception.INVALID_FILE_NAME)

        if (file.size > 10 * 1024 * 1024) {
            throw S3Exception(S3Exception.FILE_TOO_LARGE)
        }

        val fileName = UUID.randomUUID().toString() + "_$originalFilename"
        val metadata = ObjectMetadata().apply {
            contentLength = file.size
            contentType = file.contentType
        }

        return try {
            amazonS3.putObject(bucketName, fileName, file.inputStream, metadata)
            amazonS3.getUrl(bucketName, fileName).toString()
        } catch (e: Exception) {
            throw S3Exception(S3Exception.FILE_UPLOAD_ERROR)
        }
    }

    @Transactional
    fun deleteFiles(fileUrls: List<String>): List<String> {
        if (fileUrls.isEmpty()) throw S3Exception(S3Exception.FILE_DELETE_ERROR)

        fileUrls.forEach { fileUrl ->
            val fileName = extractFileNameFromUrl(fileUrl)
            amazonS3.deleteObject(bucketName, fileName)
        }
        return fileUrls
    }

    private fun extractFileNameFromUrl(fileUrl: String): String {
        return fileUrl.substringAfterLast("/")
    }
}
