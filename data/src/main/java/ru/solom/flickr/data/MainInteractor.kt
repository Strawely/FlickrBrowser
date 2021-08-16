package ru.solom.flickr.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.solom.flickrbrowser.core.IMainInteractor
import ru.solom.flickrbrowser.core.Photo
import ru.solom.flickrbrowser.core.network.IMainRepository
import ru.solom.flickrbrowser.core.network.RequestState
import javax.inject.Inject

class MainInteractor @Inject constructor(private val repository: IMainRepository) :
    IMainInteractor {
    override fun getImages(): Flow<RequestState<List<Photo>>> {
        return repository.getImages().map { state ->
            when (state) {
                is RequestState.Loading -> RequestState.Loading()
                is RequestState.Failure -> RequestState.Failure(state.throwable)
                is RequestState.Success -> {
                    RequestState.Success(state.data.map { Photo.fromDto(it) })
                }
            }
        }
    }
}
