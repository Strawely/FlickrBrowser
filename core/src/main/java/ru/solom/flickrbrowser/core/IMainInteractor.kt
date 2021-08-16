package ru.solom.flickrbrowser.core

import kotlinx.coroutines.flow.Flow
import ru.solom.flickrbrowser.core.network.RequestState

interface IMainInteractor {
    fun getImages(): Flow<RequestState<List<Photo>>>
}
