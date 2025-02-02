package com.aimory.service

import com.aimory.enums.Role
import com.aimory.exception.ChildNotFoundException
import com.aimory.exception.EmptyChildIdListException
import com.aimory.exception.EmptyPhotoIdListException
import com.aimory.exception.MemberCannotAccessChildException
import com.aimory.exception.MemberCannotAccessPhotoException
import com.aimory.exception.PhotoNotFoundException
import com.aimory.model.Child
import com.aimory.model.Photo
import com.aimory.repository.ChildRepository
import com.aimory.repository.PhotoRepository
import com.aimory.service.dto.PhotoResponseDto
import com.aimory.service.dto.toResponseDto
import com.aimory.service.dto.toResponseDtoList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val childRepository: ChildRepository,
    private val s3Service: S3Service,
) {

    @Transactional
    fun createPhotos(files: List<MultipartFile>, childId: Long): List<PhotoResponseDto> {
        val child = childRepository.findById(childId)
            .orElseThrow { ChildNotFoundException() }

        val photos = files.map { file ->
            val imageUrl = s3Service.uploadFile(file)
            Photo(imageUrl = imageUrl, child = child)
        }
        val savedPhotos = photoRepository.saveAll(photos)

        return savedPhotos.toResponseDtoList()
    }

    fun getAllPhotos(): List<PhotoResponseDto> {
        val photos = photoRepository.findAll()
        return photos.toResponseDtoList()
    }

    fun getDetailPhoto(memberId: Long, memberRole: Role, photoId: Long): PhotoResponseDto {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException() }

        checkParentCanAccessPhoto(memberId, memberRole, photo)

        return photo.toResponseDto()
    }

    fun getPhotosByChildId(memberId: Long, memberRole: Role, childId: Long): List<PhotoResponseDto> {
        val child = childRepository.findById(childId)
            .orElseThrow { ChildNotFoundException() }

        checkParentCanAccessChild(memberId, memberRole, child)

        return photoRepository.findByChildId(childId).toResponseDtoList()
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

        photoRepository.deleteAll(photos)

        return foundPhotoIds
    }

    @Transactional
    fun deletePhotosByChildId(childIds: List<Long>): List<Long> {
        if (childIds.isEmpty()) throw EmptyChildIdListException()

        val photos = photoRepository.findAllByChildIds(childIds)
        val foundChildIds = photos.map { it.child.id }.toSet()

        if (foundChildIds.isEmpty()) {
            throw ChildNotFoundException()
        }

        val imageUrls = photos.map { it.imageUrl }
        s3Service.deleteFiles(imageUrls)

        photoRepository.deleteAll(photos)

        return foundChildIds.toList()
    }

    private fun checkParentCanAccessPhoto(memberId: Long, memberRole: Role, photo: Photo) {
        if (memberRole == Role.PARENT && photo.child.parent.id != memberId) {
            throw MemberCannotAccessPhotoException()
        }
    }

    private fun checkParentCanAccessChild(memberId: Long, memberRole: Role, child: Child) {
        if (memberRole == Role.PARENT && child.parent.id != memberId) {
            throw MemberCannotAccessChildException()
        }
    }
}
