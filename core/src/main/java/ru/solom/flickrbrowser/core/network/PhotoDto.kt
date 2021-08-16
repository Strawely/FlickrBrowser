package ru.solom.flickrbrowser.core.network

import com.squareup.moshi.Json

data class PhotoDto(
    val title: String,
    @Json(name = "url_m")
    val url: String
)
