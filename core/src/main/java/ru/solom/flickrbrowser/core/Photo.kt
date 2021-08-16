package ru.solom.flickrbrowser.core

import ru.solom.flickrbrowser.core.network.PhotoDto

data class Photo(
    val title: String,
    val url: String,
    val isHighlighted: Boolean = false
) {
    companion object {
        fun fromDto(dto: PhotoDto) = Photo(
            title = dto.title,
            url = dto.url
        )

        fun Photo.toPhotoForSave() = PhotoForSave(name = title, url = url)
    }
}

/*
in so small project it may be redundant to create separate entities for different levels of Clean,
i use it only for show, that it must be used in real production projects
*/
data class PhotoForSave(
    val name: String,
    val url: String
)
