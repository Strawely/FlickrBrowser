package ru.solom.flickrbrowser.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.solom.flickrbrowser.network.Api
import ru.solom.flickrbrowser.util.RequestState
import javax.inject.Inject

typealias PhotosListState = RequestState<List<Photo>>

interface IMainInteractor {
    suspend fun getImages(): Flow<PhotosListState>
}

class MainInteractor @Inject constructor(private val api: Api) : IMainInteractor {
    override suspend fun getImages() = flow<PhotosListState> {
        emit(RequestState.Loading())
        try {
            val photosDto = api.getImages()
            val result = photosDto.photos.photo.map { Photo.fromDto(it) }
            emit(RequestState.Success(result))
        } catch (e: Exception) {
            emit(RequestState.Failure(e))
        }

    }
}