package ru.solom.flickrbrowser.core.file

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.solom.flickrbrowser.core.Event

interface IFileHandler {
    /**
     * indicates events of file saving, `true` means successful saving `false` mean some errors
     */
    val fileEvents: StateFlow<Event<Int>?>
    fun saveFile(uri: Uri)
    fun initPhotoSaving()
}
