package ru.solom.flickrbrowser.main.file

import android.net.Uri
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.solom.flickrbrowser.core.ActivityProvider
import ru.solom.flickrbrowser.core.Event
import ru.solom.flickrbrowser.core.PhotoForSave
import ru.solom.flickrbrowser.core.file.IFileHandler
import ru.solom.flickrbrowser.main.R
import ru.solom.flickrbrowser.main.ui.FileActivity
import java.io.Closeable

class FileHandler(
    private val activityProvider: ActivityProvider,
    private val fileApi: FileApi,
    private val photo: PhotoForSave
) : IFileHandler {
    private val activity get() = activityProvider.requireActivity()

    private val _fileEvents = MutableStateFlow<Event<@StringRes Int>?>(null)
    override val fileEvents = _fileEvents.asStateFlow()
    private var job: Job? = null

    override fun initPhotoSaving() {
        val extension = photo.url.substringAfterLast('.')
        (activity as FileActivity).fileCreateLauncher?.launch("${photo.name}.$extension")
    }

    override fun saveFile(uri: Uri) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val body = fileApi.getFile(photo.url)
            var isSuccessful = false // to workaround "redundant initializer" one line above
            body.byteStream().use({ inputStream ->
                activity.contentResolver.openOutputStream(uri).use(outStream@{ outputStream ->
                    if (outputStream == null) return@outStream
                    val buffer = ByteArray(DEFAULT_BUFFER)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                }, outStream@{ return@launch })
                isSuccessful = true
            }, { return@launch })
            _fileEvents.value = Event(
                if (isSuccessful) {
                    R.string.file_saved
                } else {
                    R.string.file_save_error
                }
            )
        }
    }

    private inline fun <T : Closeable?, R> T.use(block: (T) -> R, catch: (e: Throwable) -> R): R {
        return try {
            this.use(block)
        } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
            catch(e)
        }
    }
}

private const val DEFAULT_BUFFER = 4 * 1024
