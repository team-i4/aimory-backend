package com.aimory.controller

import com.aimory.controller.dto.DeleteRequest
import com.aimory.controller.dto.DeleteResponse
import com.aimory.controller.dto.PhotoListResponse
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toResponse
import com.aimory.exception.InvalidChildIdException
import com.aimory.exception.InvalidPhotoUploadException
import com.aimory.service.PhotoService
import com.aimory.service.dto.toRequestDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class PhotoController(
    private val photoService: PhotoService,
) {

    @PostMapping("/photos", consumes = ["multipart/form-data"])
    fun createPhotos(
        @RequestParam("files") files: List<MultipartFile>,
        @RequestParam("childId") childId: Long,
    ): List<PhotoResponse> {
        if (files.isEmpty()) throw InvalidPhotoUploadException()
        if (childId <= 0) throw InvalidChildIdException()

        val photoDtoList = photoService.createPhotos(files.toRequestDto(childId))
        return photoDtoList.toResponse()
    }

    @GetMapping("/photos")
    fun getAllPhotos(): PhotoListResponse {
        val photoListDto = photoService.getAllPhotos()
        val photoCount = photoService.getAllPhotosCount()
        val photoListResponse = photoListDto.map { it.toResponse() }
        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @GetMapping("/photos/{photoId}")
    fun getDetailPhoto(@PathVariable photoId: Long): PhotoResponse {
        val photoDto = photoService.getDetailPhoto(photoId)
        return photoDto.toResponse()
    }

    @GetMapping("/photos/child")
    fun getPhotosByChildId(@RequestParam("childId") childId: Long): PhotoListResponse {
        val photoListDto = photoService.getPhotosByChildId(childId)
        val photoCount = photoService.getPhotoCountByChildId(childId)
        val photoListResponse = photoListDto.map { it.toResponse() }
        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @DeleteMapping("/photos")
    fun deletePhotos(@RequestBody deleteRequest: DeleteRequest): DeleteResponse {
        return DeleteResponse(photoService.deletePhotos(deleteRequest.data))
    }

    @DeleteMapping("/photos/child")
    fun deletePhotosByChildId(@RequestBody deleteRequest: DeleteRequest): DeleteResponse {
        return DeleteResponse(photoService.deletePhotosByChildId(deleteRequest.data))
    }
}
