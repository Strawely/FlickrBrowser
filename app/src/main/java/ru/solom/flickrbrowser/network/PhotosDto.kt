package ru.solom.flickrbrowser.network

import com.squareup.moshi.Json

data class PhotosDto(
    val photos: PhotosInnerDto
)

data class PhotosInnerDto (
    val photo: List<PhotoDto>
)

data class PhotoDto(
    val title: String,
    @Json(name = "url_m")
    val url: String
)
