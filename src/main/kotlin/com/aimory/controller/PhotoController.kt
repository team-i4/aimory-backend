package com.aimory.controller

import com.aimory.controller.dto.PhotoAlbumResponse
import com.aimory.controller.dto.PhotoListResponse
import com.aimory.service.dto.PhotoDeleteRequest
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toPhotoResponse
import com.aimory.service.PhotoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createPhotos(
        @RequestParam("files") files: List<MultipartFile>,
        @RequestParam("childId") childId: Long
    ): PhotoListResponse {
        require(files.isNotEmpty()) { "업로드할 파일이 없습니다." }
        require(childId > 0) { "올바른 원아 ID를 입력하세요." }

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

