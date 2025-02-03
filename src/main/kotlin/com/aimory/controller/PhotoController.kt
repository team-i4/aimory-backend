package com.aimory.controller

import com.aimory.controller.dto.DeleteChildPhotoResponse
import com.aimory.controller.dto.DeletePhotoResponse
import com.aimory.controller.dto.DeleteRequest
import com.aimory.controller.dto.PhotoListResponse
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toResponse
import com.aimory.exception.InvalidChildIdException
import com.aimory.exception.InvalidPhotoUploadException
import com.aimory.security.JwtAuthentication
import com.aimory.service.PhotoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun getAllPhotos(
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String,
    ): PhotoListResponse {
        val sort = Sort.by(
            if (sortDirection.equals("ASC", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            },
            sortBy
        )

        val photoListDto = photoService.getAllPhotos(sort)
        val photoCount = photoListDto.size
        val photoListResponse = photoListDto.map { it.toResponse() }
        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @GetMapping("/photos/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "단일 사진 조회 API")
    fun getDetailPhoto(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @PathVariable photoId: Long,
    ): PhotoResponse {
        val memberId = authentication.id
        val memberRole = authentication.role
        val photoDto = photoService.getDetailPhoto(memberId, memberRole, photoId)
        return photoDto.toResponse()
    }

    @GetMapping("/photos/child")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "특정 원아 사진 조회 API")
    fun getPhotosByChildId(
        @AuthenticationPrincipal authentication: JwtAuthentication,
        @RequestParam("childId") childId: Long,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String,
    ): PhotoListResponse {
        val memberId = authentication.id
        val memberRole = authentication.role
        val sort = Sort.by(
            if (sortDirection.equals("ASC", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            },
            sortBy
        )

        val photoListDto = photoService.getPhotosByChildId(memberId, memberRole, childId, sort)
        val photoCount = photoListDto.size
        val photoListResponse = photoListDto.map { it.toResponse() }

        return PhotoListResponse(photos = photoListResponse, totalCount = photoCount)
    }

    @DeleteMapping("/photos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "사진 삭제 API")
    fun deletePhotos(@RequestBody deleteRequest: DeleteRequest): DeletePhotoResponse {
        val deletedPhotoIds = photoService.deletePhotos(deleteRequest.data)
        return DeletePhotoResponse(deletedPhotoIds = deletedPhotoIds)
    }

    @DeleteMapping("/photos/child")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "특정 원아 사진 삭제 API")
    fun deletePhotosByChildId(@RequestBody deleteRequest: DeleteRequest): DeleteChildPhotoResponse {
        val deletedChildIds = photoService.deletePhotosByChildId(deleteRequest.data)
        return DeleteChildPhotoResponse(deletedPhotosChildId = deletedChildIds)
    }
}
