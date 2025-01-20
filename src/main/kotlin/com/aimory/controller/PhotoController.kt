package com.aimory.controller

import com.aimory.controller.dto.PhotoAlbumResponse
import com.aimory.service.dto.PhotoDeleteRequest
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toPhotoResponse
import com.aimory.service.PhotoService
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<List<PhotoResponse>> {
        require(files.isNotEmpty()) { "업로드할 파일이 없습니다." }
        require(childId > 0) { "올바른 원아 ID를 입력하세요." }

        val photos = photoService.createPhotos(files, childId)
        return ResponseEntity.ok(photos.map { it.toPhotoResponse(photoCount = photos.size) })
    }

    @GetMapping("/list")
    fun getPhotoAlbums(): ResponseEntity<List<PhotoAlbumResponse>> {
        val photoAlbums = photoService.getPhotoAlbums()
        return ResponseEntity.ok(photoAlbums)
    }

    @GetMapping("/{photoId}")
    fun getPhotoById(@PathVariable photoId: Long): ResponseEntity<PhotoResponse> {
        return ResponseEntity.ok(photoService.getPhotoById(photoId).toPhotoResponse(photoCount = 1))
    }

    @GetMapping
    fun getPhotosByChildId(@RequestParam("childId", required = false) childId: Long?): ResponseEntity<List<PhotoResponse>> {
        val photos = if (childId == null) {
            photoService.getAllPhotos()
        } else {
            photoService.getPhotosByChildId(childId)
        }
        return ResponseEntity.ok(photos)
    }

    @DeleteMapping
    fun deletePhotos(@RequestBody request: PhotoDeleteRequest): ResponseEntity<Map<String, String>> {
        photoService.deletePhotosByType(request.type, request.data.mapNotNull { it.photoId ?: it.childId })
        return ResponseEntity.ok(mapOf("message" to "사진이 성공적으로 삭제되었습니다."))
    }
}

