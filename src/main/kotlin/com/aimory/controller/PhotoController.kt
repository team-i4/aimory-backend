package com.aimory.controller

import com.aimory.controller.dto.PhotoAlbumResponse
import com.aimory.controller.dto.PhotoDeleteRequest
import com.aimory.controller.dto.PhotoListResponse
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toPhotoResponse
import com.aimory.exception.InvalidChildIdException
import com.aimory.exception.InvalidPhotoUploadException
import com.aimory.service.PhotoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService,
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createPhotos(
        @RequestParam("files") files: List<MultipartFile>,
        @RequestParam("childId") childId: Long,
    ): PhotoListResponse {
        if (files.isEmpty()) throw InvalidPhotoUploadException()
        if (childId <= 0) throw InvalidChildIdException()

        val photos = photoService.createPhotos(files, childId)
        val photoResponses = photos.map { it.toPhotoResponse() }

        val response = PhotoListResponse(
            childId = childId,
            photoCount = photoResponses.size,
            photos = photoResponses
        )

        return (response)
    }

    @GetMapping("/list")
    fun getPhotoAlbums(): List<PhotoAlbumResponse> {
        val photoAlbums = photoService.getPhotoAlbums()
        return (photoAlbums)
    }

    @GetMapping("/{photoId}")
    fun getPhotoById(@PathVariable photoId: Long): PhotoResponse {
        return (photoService.getPhotoById(photoId))
    }

    @GetMapping
    fun getPhotosByChildId(@RequestParam("childId", required = false) childId: Long?): List<PhotoResponse> {
        val photos = if (childId == null) {
            photoService.getAllPhotos()
        } else {
            photoService.getPhotosByChildId(childId)
        }
        return (photos)
    }

    @DeleteMapping
    fun deletePhotos(@RequestBody request: PhotoDeleteRequest): Map<String, String> {
        photoService.deletePhotosByType(request.type, request.data.mapNotNull { it.photoId ?: it.childId })
        return (mapOf("message" to "사진이 성공적으로 삭제되었습니다."))
    }
}
