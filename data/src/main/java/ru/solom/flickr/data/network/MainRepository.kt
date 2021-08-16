package ru.solom.flickr.data.network

import kotlinx.coroutines.flow.flow
import ru.solom.flickrbrowser.core.network.FlickrBuildInfo
import ru.solom.flickrbrowser.core.network.IMainRepository
import ru.solom.flickrbrowser.core.network.PhotoDtoListState
import ru.solom.flickrbrowser.core.network.RequestState
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: Api,
    private val buildInfo: FlickrBuildInfo
) : IMainRepository {
    override fun getImages() = flow<PhotoDtoListState> {
        emit(RequestState.Loading())
        try {
            val photosDto = api.getImages(buildInfo.apiKey)
            val result = photosDto.photos.photo
            emit(RequestState.Success(result))
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            emit(RequestState.Failure(e))
        }
    }
}
