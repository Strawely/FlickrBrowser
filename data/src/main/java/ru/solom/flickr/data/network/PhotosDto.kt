package ru.solom.flickr.data.network

import ru.solom.flickrbrowser.core.network.PhotoDto

data class PhotosDto(
    val photos: PhotosInnerDto
)

data class PhotosInnerDto(
    val photo: List<PhotoDto>
)
