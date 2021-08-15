package ru.solom.flickrbrowser.file

import android.net.Uri
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.solom.flickrbrowser.ActivityProvider
import ru.solom.flickrbrowser.R
import ru.solom.flickrbrowser.ui.FileActivity
import ru.solom.flickrbrowser.util.Event

interface IFileHandler {
    /**
     * indicates events of file saving, `true` means successful saving `false` mean some errors
     */
    val fileEvents: StateFlow<Event<Int>?>
    fun saveFile(uri: Uri)
    fun initPhotoSaving()
}

class FileHandler(
    private val activityProvider: ActivityProvider,
    private val fileApi: FileApi,
    private val photo: PhotoForSave
) : IFileHandler {
    val activity get() = activityProvider.requireActivity()

    private val _fileEvents = MutableStateFlow<Event<@StringRes Int>?>(null)
    override val fileEvents = _fileEvents.asStateFlow()
    private var job: Job? = null

    override fun initPhotoSaving() {
        val extension = photo.url.substringAfterLast('.')
        (activity as FileActivity).fileCreateLauncher?.launch("${photo.name}.$extension")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun saveFile(uri: Uri) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val body = fileApi.getFile(photo.url)
            var isSuccessful: Boolean
            isSuccessful = false // to workaround "redundant initializer" one line above
            body.byteStream().use { inputStream ->
                activity.contentResolver.openOutputStream(uri).use outStream@{ outputStream ->
                    if (outputStream == null) return@outStream
                    val buffer = ByteArray(DEFAULT_BUFFER)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                }
                isSuccessful = true
            }
            _fileEvents.value = Event(
                if (isSuccessful) {
                    R.string.file_saved
                } else {
                    R.string.file_save_error
                }
            )
        }
    }
}

/*
in so small project it may be redundant to create separate entities for different levels of Clean,
i use it only for show, that it must be used in real production projects
*/
data class PhotoForSave(
    val name: String,
    val url: String
)

private const val DEFAULT_BUFFER = 4 * 1024
