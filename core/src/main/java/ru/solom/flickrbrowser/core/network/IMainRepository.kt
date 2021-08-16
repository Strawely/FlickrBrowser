package ru.solom.flickrbrowser.core.network

import kotlinx.coroutines.flow.Flow

typealias PhotoDtoListState = RequestState<List<PhotoDto>>

interface IMainRepository {
    fun getImages(): Flow<PhotoDtoListState>
}
