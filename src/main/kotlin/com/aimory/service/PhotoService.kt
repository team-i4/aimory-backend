package com.aimory.service

import com.aimory.controller.dto.PhotoAlbumResponse
import com.aimory.controller.dto.PhotoResponse
import com.aimory.controller.dto.toPhotoAlbumResponse
import com.aimory.controller.dto.toPhotoResponse
import com.aimory.model.Photo
import com.aimory.repository.ChildRepository
import com.aimory.repository.PhotoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val childRepository: ChildRepository
) {

    @Transactional
    fun createPhotos(files: List<MultipartFile>, childId: Long): List<Photo> {
        val child = childRepository.findById(childId).orElseThrow {
            IllegalArgumentException("해당 원아를 찾을 수 없습니다.")
        }

        val photos = files.map { file ->
            Photo(imageUrl = "local-storage/${file.originalFilename}", child = child)
        }

        return photoRepository.saveAll(photos)
    }

    fun getPhotoAlbums(): List<PhotoAlbumResponse> {
        val photos = photoRepository.findAll()

        val allPhotoAlbum = photos.toPhotoAlbumResponse(childId = 0, profileImageUrl = "")

        val groupedPhotos = photos.groupBy { it.child }
            .map { (child, childPhotos) ->
                childPhotos.toPhotoAlbumResponse(child.id, child.profileImageUrl)
            }

        return listOf(allPhotoAlbum) + groupedPhotos
    }

    fun getPhotoById(photoId: Long): PhotoResponse {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { IllegalArgumentException("해당 사진이 존재하지 않습니다.") }
        return photo.toPhotoResponse()
    }

    fun getAllPhotos(): List<PhotoResponse> {
        val photos = photoRepository.findAll()
        return photos.map { photo ->
            val photoCount = photoRepository.countByChildId(photo.child.id)
            photo.toPhotoResponse()
        }
    }

    fun getPhotosByChildId(childId: Long): List<PhotoResponse> {
        val photos = photoRepository.findByChildId(childId)
        val photoCount = photoRepository.countByChildId(childId)
        return photos.map { photo ->
            photo.toPhotoResponse()
        }
    }

    @Transactional
    fun deletePhotosByType(type: String, ids: List<Long>) {
        when (type) {
            "photo" -> photoRepository.deleteAllById(ids)
            "child" -> ids.forEach { photoRepository.deleteAllByChildId(it) }
            "all" -> photoRepository.deleteAll()
            else -> throw IllegalArgumentException("올바른 삭제 타입이 아닙니다.")
        }
    }
}
