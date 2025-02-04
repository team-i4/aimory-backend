package com.aimory.service

import com.aimory.exception.CompareFacesFailException
import com.aimory.exception.DetectFacesFailException
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.Attribute
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest
import software.amazon.awssdk.services.rekognition.model.DetectFacesRequest
import software.amazon.awssdk.services.rekognition.model.Image

@Service
class RekognitionService(
    private val rekognitionClient: RekognitionClient,
) {

    fun detectFaces(imageBytes: ByteArray): Boolean {
        val image = Image.builder()
            .bytes(SdkBytes.fromByteArray(imageBytes))
            .build()

        val request = DetectFacesRequest.builder()
            .image(image)
            .attributes(Attribute.ALL)
            .build()

        return try {
            val response = rekognitionClient.detectFaces(request)
            response.faceDetails().isNotEmpty()
        } catch (e: software.amazon.awssdk.services.rekognition.model.RekognitionException) {
            throw DetectFacesFailException("얼굴 인식 실패: ${e.message}", e)
        }
    }

    fun compareFaces(sourceImageBytes: ByteArray, targetImageBytes: ByteArray): Float? {
        val sourceImage = Image.builder().bytes(SdkBytes.fromByteArray(sourceImageBytes)).build()
        val targetImage = Image.builder().bytes(SdkBytes.fromByteArray(targetImageBytes)).build()

        val request = CompareFacesRequest.builder()
            .sourceImage(sourceImage)
            .targetImage(targetImage)
            .similarityThreshold(90f)
            .build()

        return try {
            val response = rekognitionClient.compareFaces(request)
            response.faceMatches().firstOrNull()?.similarity()
        } catch (e: software.amazon.awssdk.services.rekognition.model.RekognitionException) {
            throw CompareFacesFailException("얼굴 비교 실패: ${e.message}", e)
        }
    }
}
