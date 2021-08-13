package ru.solom.flickrbrowser.network

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import ru.solom.flickrbrowser.BuildConfig

interface Api {
    @GET(
        "rest?method=flickr.photos.search" +
                "&tags=Electrolux" +
                "&format=json" +
                "&nojsoncallback=true" +
                "&extras=media" +
                "&extras=url_sq&extras=url_m"
    )
    suspend fun getImages(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("per_page") pageSize: Int = 21,
        @Query("page") pageNum: Int = 1
    ): PhotosDto
}