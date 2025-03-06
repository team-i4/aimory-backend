package com.aimory.service

import com.aimory.controller.dto.AssignPhotoRequest
import com.aimory.enums.PhotoStatus
import com.aimory.enums.Role
import com.aimory.exception.ChildNotFoundException
import com.aimory.exception.EmptyChildIdListException
import com.aimory.exception.EmptyPhotoIdListException
import com.aimory.exception.MemberCannotAccessChildException
import com.aimory.exception.MemberCannotAccessPhotoException
import com.aimory.exception.PendingPhotosNotFoundException
import com.aimory.exception.PhotoNotFoundException
import com.aimory.model.Child
import com.aimory.model.Photo
import com.aimory.model.PhotoChild
import com.aimory.repository.ChildRepository
import com.aimory.repository.PhotoChildRepository
import com.aimory.repository.PhotoRepository
import com.aimory.service.dto.PhotoResponseDto
import com.aimory.service.dto.toResponseDto
import com.aimory.service.dto.toResponseDtoList
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val childRepository: ChildRepository,
    private val photoChildRepository: PhotoChildRepository,
    private val s3Service: S3Service,
    private val rekognitionService: RekognitionService,
) {

    @Transactional
    fun createPhotos(files: List<MultipartFile>): List<PhotoResponseDto> {
        val photos = mutableListOf<Photo>()

        for (file in files) {
            val imageBytes = file.bytes
            val hasFace = rekognitionService.detectFaces(imageBytes)
            val imageUrl = s3Service.uploadFile(file)

            if (imageUrl.isNullOrBlank()) {
                println("업로드 실패: 파일이 S3에 저장되지 않았음.")
                continue
            }

            val matchedChildren = if (hasFace) {
                val matchedChildIds = findMatchingChildren(imageBytes)
                matchedChildIds.mapNotNull { childRepository.findById(it).orElse(null) }.toMutableList()
            } else {
                mutableListOf()
            }

            val status = if (matchedChildren.isNotEmpty()) PhotoStatus.CONFIRMED else PhotoStatus.PENDING
            val photo = photoRepository.save(Photo(imageUrl, status))

            matchedChildren.forEach { child ->
                photoChildRepository.save(PhotoChild(photo = photo, child = child))
            }

            photos.add(photo)
        }

        if (photos.isEmpty()) {
            println("저장할 사진이 없습니다.")
            return emptyList()
        }

        val savedPhotos = photoRepository.saveAll(photos)
        return savedPhotos.toResponseDtoList()
    }

    fun getAllPhotos(sort: Sort): List<PhotoResponseDto> {
        val photos = photoRepository.findAll(sort)
        return photos.toResponseDtoList()
    }

    fun getDetailPhoto(memberId: Long, memberRole: Role, photoId: Long): PhotoResponseDto {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException() }

        checkParentCanAccessPhoto(memberId, memberRole, photo)

        return photo.toResponseDto()
    }

    fun getPhotosByChildId(memberId: Long, memberRole: Role, childId: Long, sort: Sort): List<PhotoResponseDto> {
        val child = childRepository.findById(childId)
            .orElseThrow { ChildNotFoundException() }

        checkParentCanAccessChild(memberId, memberRole, child)

        return photoChildRepository.findByChildId(childId, sort)
            .map { it.photo }
            .toResponseDtoList()
    }

    @Transactional
    fun deletePhotos(photoIds: List<Long>): List<Long> {
        if (photoIds.isEmpty()) throw EmptyPhotoIdListException()

        val photos = photoRepository.findAllById(photoIds)
        val foundPhotoIds = photos.map { it.id }

        if (foundPhotoIds.isEmpty()) {
            throw PhotoNotFoundException()
        }

        val imageUrls = photos.map { it.imageUrl }
        s3Service.deleteFiles(imageUrls)

        photoIds.forEach { photoId ->
            photoChildRepository.deleteByPhotoId(photoId)
        }

        photoRepository.deleteAll(photos)

        return foundPhotoIds
    }

    @Transactional
    fun deletePhotosByChildId(childIds: List<Long>): List<Long> {
        if (childIds.isEmpty()) throw EmptyChildIdListException()

        childIds.forEach { childId ->
            photoChildRepository.deleteByChildId(childId)
        }

        val orphanPhotoIds = photoRepository.findAll()
            .filter { it.photoChildren.isEmpty() }
            .map { it.id }

        if (orphanPhotoIds.isNotEmpty()) {
            photoRepository.deleteAllById(orphanPhotoIds)
        }

        return childIds
    }

    private fun findMatchingChildren(imageBytes: ByteArray): List<Long> {
        val children = childRepository.findAll()
        val matchedChildren = mutableListOf<Long>()

        for (child in children) {
            val profileImageBytes = child.profileImageUrl?.let { s3Service.downloadFile(it) }

            if (profileImageBytes == null) {
                println("프로필 사진이 없는 원아: ${child.id}")
                continue
            }

            val similarity = rekognitionService.compareFaces(profileImageBytes, imageBytes)
            if (similarity != null && similarity >= 90.0f) {
                matchedChildren.add(child.id)
            }
        }
        return matchedChildren
    }

    fun getPendingPhotos(sort: Sort): List<PhotoResponseDto> {
        val pendingPhotos = photoRepository.findAllByStatus(PhotoStatus.PENDING, sort)

        if (pendingPhotos.isEmpty()) {
            throw PendingPhotosNotFoundException()
        }

        return pendingPhotos.toResponseDtoList()
    }

    @Transactional
    fun assignPhotos(request: AssignPhotoRequest): List<PhotoResponseDto> {
        val children = childRepository.findByNameIn(request.childNames)
        if (children.isEmpty()) {
            throw ChildNotFoundException()
        }

        val pendingPhotos = photoRepository.findByStatus(PhotoStatus.PENDING)

        if (pendingPhotos.isEmpty()) {
            throw PendingPhotosNotFoundException()
        }

        val photos = pendingPhotos.filter { it.id in request.photoIds }

        if (photos.isEmpty()) {
            throw PendingPhotosNotFoundException()
        }

        photos.forEach { photo ->
            children.forEach { child -> photo.addChild(child) }
            photo.changeStatus(PhotoStatus.CONFIRMED)
        }

        val savedPhotos = photoRepository.saveAll(photos)
        return savedPhotos.toResponseDtoList()
    }

    private fun checkParentCanAccessPhoto(memberId: Long, memberRole: Role, photo: Photo) {
        val firstChild = photoChildRepository.findByPhotoId(photo.id).firstOrNull()?.child
            ?: throw MemberCannotAccessPhotoException()

        if (memberRole == Role.PARENT && firstChild.parent.id != memberId) {
            throw MemberCannotAccessPhotoException()
        }
    }

    private fun checkParentCanAccessChild(memberId: Long, memberRole: Role, child: Child) {
        if (memberRole == Role.PARENT && child.parent.id != memberId) {
            throw MemberCannotAccessChildException()
        }
    }
}
