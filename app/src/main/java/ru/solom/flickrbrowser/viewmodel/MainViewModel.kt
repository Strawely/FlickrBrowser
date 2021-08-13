package ru.solom.flickrbrowser.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.solom.flickrbrowser.interactor.IMainInteractor
import ru.solom.flickrbrowser.interactor.Photo
import ru.solom.flickrbrowser.util.RequestState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: IMainInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun update() {
        viewModelScope.launch {
            interactor.getImages().collect { result ->
                val error = if (result is RequestState.Failure) {
                    result.throwable.localizedMessage
                } else {
                    null
                }
                _state.value = _state.value.copy(
                    loading = result is RequestState.Loading,
                    error = error,
                    list = if (result is RequestState.Success) result.data else null
                )
            }
        }
    }

    fun onItemClick(photo: Photo) = _state.value.list?.let { list ->
        val oldPhotoIdx = list.indexOf(photo)
        if (list[oldPhotoIdx].isHighlighted) savePhoto(list[oldPhotoIdx])
        val newList = list.mapIndexed { i, photo ->
            when {
                i == oldPhotoIdx -> photo.changeHighlighting()
                photo.isHighlighted -> photo.changeHighlighting()
                else -> photo
            }
        }
        _state.value = _state.value.copy(list = newList)
    }

    private fun Photo.changeHighlighting() = copy(isHighlighted = !isHighlighted)

    private fun savePhoto(photo: Photo) {
        Log.d("===", "${photo.url} saved")
    }
}

data class MainState(
    val loading: Boolean = false,
    val list: List<Photo>? = null,
    val error: String? = null
)
