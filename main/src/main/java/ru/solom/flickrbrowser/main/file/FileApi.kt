package ru.solom.flickrbrowser.main.file

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileApi {
    @GET
    @Streaming
    suspend fun getFile(@Url url: String): ResponseBody
}
