package ru.solom.flickrbrowser.viewmodel

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.solom.flickrbrowser.core.Event
import ru.solom.flickrbrowser.core.IMainInteractor
import ru.solom.flickrbrowser.core.Photo
import ru.solom.flickrbrowser.core.Photo.Companion.toPhotoForSave
import ru.solom.flickrbrowser.core.file.IFileHandler
import ru.solom.flickrbrowser.core.file.IFileHandlerFactory
import ru.solom.flickrbrowser.core.network.RequestState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: IMainInteractor,
    private val fileHandlerFactory: IFileHandlerFactory
) : ViewModel() {

    private var fileHandler: IFileHandler? = null

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _fileEvents = MutableStateFlow<Event<@StringRes Int>?>(null)
    val fileEvents = _fileEvents.asStateFlow()

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

    fun onPhotoFileCreated(uri: Uri) {
        viewModelScope.launch { fileHandler?.saveFile(uri) }
    }

    private fun Photo.changeHighlighting() = copy(isHighlighted = !isHighlighted)

    private fun savePhoto(photo: Photo) {
        fileHandler = fileHandlerFactory.create(photo.toPhotoForSave()).apply {
            initPhotoSaving()
            viewModelScope.launch {
                fileEvents.collect { _fileEvents.value = it }
            }
        }
    }
}

data class MainState(
    val loading: Boolean = false,
    val list: List<Photo>? = null,
    val error: String? = null
)
