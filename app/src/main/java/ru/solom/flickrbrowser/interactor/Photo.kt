package ru.solom.flickrbrowser.interactor

import ru.solom.flickrbrowser.network.PhotoDto

data class Photo(
    val title: String,
    val url: String
) {
    companion object {
        fun fromDto(dto: PhotoDto) = Photo(
            title = dto.title,
            url = dto.url
        )
    }
}
