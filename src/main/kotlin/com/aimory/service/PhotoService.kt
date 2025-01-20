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
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val childRepository: ChildRepository
) {

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

        val allPhotos = photos.map { it.imageUrl }
        val allPhotoAlbum = PhotoAlbumResponse(
            childId = 0,
//            name = "전체",
            photoCount = allPhotos.size,
            photos = allPhotos,
            profileImageUrl = ""    // 전체 사진 묶음에 대한 대표 이미지 필요
        )

        val groupedPhotos = photos.groupBy { it.child }
            .map { (child, childPhotos) ->
                PhotoAlbumResponse(
                    childId = child.id,
//                    name = child.name,
                    photoCount = childPhotos.size,
                    photos = childPhotos.map { it.imageUrl },
                    profileImageUrl = child.profileImageUrl
                )
            }

        return listOf(allPhotoAlbum) + groupedPhotos
    }

    fun getPhotoById(photoId: Long): Photo {
        return photoRepository.findById(photoId)
            .orElseThrow { IllegalArgumentException("해당 사진이 존재하지 않습니다.") }
    }

    fun getAllPhotos(): List<PhotoResponse> {
        val photos = photoRepository.findAll()
        return photos.map { photo ->
            val photoCount = photoRepository.countByChildId(photo.child.id)
            photo.toPhotoResponse(photoCount)
        }
    }

    fun getPhotosByChildId(childId: Long): List<PhotoResponse> {
        val photos = photoRepository.findByChildId(childId)
        val photoCount = photoRepository.countByChildId(childId)
        return photos.map { photo ->
            photo.toPhotoResponse(photoCount)
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
