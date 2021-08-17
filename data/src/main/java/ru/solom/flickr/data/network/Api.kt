package ru.solom.flickr.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET(
        "rest?method=flickr.photos.search" +
                "&format=json" +
                "&nojsoncallback=true" +
                "&extras=media" +
                "&extras=url_sq&extras=url_m"
    )
    suspend fun getImages(
        @Query("api_key") apiKey: String,
        @Query("per_page") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("page") pageNum: Int = START_PAGE,
        @Query("tags") tag: String = ELECTROLUX_TAG
    ): PhotosDto
}

private const val DEFAULT_PAGE_SIZE = 21
private const val START_PAGE = 1
private const val ELECTROLUX_TAG = "Electrolux"
