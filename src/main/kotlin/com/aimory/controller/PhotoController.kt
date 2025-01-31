package com.aimory.controller

import com.aimory.controller.dto.DeleteChildPhotoResponse
import com.aimory.controller.dto.DeletePhotoResponse
import com.aimory.controller.dto.DeleteRequest
import com.aimory.controller.dto.PhotoListResponse
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toResponse
import com.aimory.exception.InvalidChildIdException
import com.aimory.exception.InvalidPhotoUploadException
import com.aimory.service.PhotoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class PhotoController(
    private val photoService: PhotoService,
) {

    @PostMapping("/photos", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "사진 업로드 API")
    fun createPhotos(
        @RequestParam("files") files: List<MultipartFile>,
        @RequestParam("childId") childId: Long,
    ): List<PhotoResponse> {
        if (files.isEmpty()) throw InvalidPhotoUploadException()
        if (childId <= 0) throw InvalidChildIdException()

        val photoDtoList = photoService.createPhotos(files, childId)
        return photoDtoList.toResponse()
    }

    @GetMapping("/photos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "전체 사진 조회 API")
    fun getAllPhotos(): PhotoListResponse {
        val photoListDto = photoService.getAllPhotos()
        val photoCount = photoService.getAllPhotosCount()
        val photoListResponse = photoListDto.map { it.toResponse() }
        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @GetMapping("/photos/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "단일 사진 조회 API")
    fun getDetailPhoto(@PathVariable photoId: Long): PhotoResponse {
        val photoDto = photoService.getDetailPhoto(photoId)
        return photoDto.toResponse()
    }

    @GetMapping("/photos/child")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "특정 원아 사진 조회 API")
    fun getPhotosByChildId(@RequestParam("childId") childId: Long): PhotoListResponse {
        val photoListDto = photoService.getPhotosByChildId(childId)
        val photoCount = photoService.getPhotoCountByChildId(childId)
        val photoListResponse = photoListDto.map { it.toResponse() }
        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @DeleteMapping("/photos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사진 삭제 API")
    fun deletePhotos(@RequestBody deleteRequest: DeleteRequest): DeletePhotoResponse {
        return DeletePhotoResponse(deletedPhotoIds = photoService.deletePhotos(deleteRequest.data))
    }

    @DeleteMapping("/photos/child")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "특정 원아 사진 삭제 API")
    fun deletePhotosByChildId(@RequestBody deleteRequest: DeleteRequest): DeleteChildPhotoResponse {
        return DeleteChildPhotoResponse(deletedPhotosChildId = photoService.deletePhotosByChildId(deleteRequest.data))
    }
}
