package com.aimory.service

import com.aimory.exception.ChildNotFoundException
import com.aimory.exception.EmptyChildIdListException
import com.aimory.exception.EmptyPhotoIdListException
import com.aimory.exception.NonExistentChildIdException
import com.aimory.exception.PhotoNotFoundException
import com.aimory.repository.ChildRepository
import com.aimory.repository.PhotoRepository
import com.aimory.service.dto.PhotoRequestDto
import com.aimory.service.dto.PhotoResponseDto
import com.aimory.service.dto.toEntity
import com.aimory.service.dto.toResponseDto
import com.aimory.service.dto.toResponseDtoList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val childRepository: ChildRepository,
) {

    @Transactional
    fun createPhotos(photoRequestDto: PhotoRequestDto): List<PhotoResponseDto> {
        val child = childRepository.findById(photoRequestDto.childId)
            .orElseThrow { ChildNotFoundException() }

        val photos = photoRequestDto.toEntity(child)
        val savedPhotos = photoRepository.saveAll(photos)

        return savedPhotos.toResponseDtoList()
    }

    fun getAllPhotos(): List<PhotoResponseDto> {
        val photos = photoRepository.findAll()
        return photos.toResponseDtoList()
    }

    fun getAllPhotosCount(): Int {
        return photoRepository.count().toInt()
    }

    fun getDetailPhoto(photoId: Long): PhotoResponseDto {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException() }
        return photo.toResponseDto()
    }

    fun getPhotosByChildId(childId: Long): List<PhotoResponseDto> {
        if (!childRepository.existsById(childId)) {
            throw ChildNotFoundException()
        }
        val photos = photoRepository.findByChildId(childId)
        return photos.toResponseDtoList()
    }

    fun getPhotoCountByChildId(childId: Long): Int {
        if (!childRepository.existsById(childId)) {
            throw ChildNotFoundException()
        }
        return photoRepository.countByChildId(childId)
    }

    @Transactional
    fun deletePhotos(photoIds: List<Long>): String {
        if (photoIds.isEmpty()) throw EmptyPhotoIdListException()

        photoRepository.deleteByPhotoIds(photoIds)
        return "선택한 사진이 성공적으로 삭제되었습니다."
    }

    @Transactional
    fun deletePhotosByChildId(childIds: List<Long>): String {
        if (childIds.isEmpty()) throw EmptyChildIdListException()

        childIds.forEach { childId ->
            if (!childRepository.existsById(childId)) {
                throw NonExistentChildIdException(childId)
            }
            photoRepository.deleteByChildId(childId)
        }
        return "선택한 원아의 사진이 성공적으로 삭제되었습니다."
    }
}
