package ru.solom.flickrbrowser.core

import kotlinx.coroutines.flow.Flow
import ru.solom.flickrbrowser.core.network.RequestState

typealias PhotoListState = RequestState<List<Photo>>

interface IMainInteractor {
    fun getImages(): Flow<PhotoListState>
}
