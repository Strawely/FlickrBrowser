package ru.solom.flickrbrowser.file

import ru.solom.flickrbrowser.ActivityProvider
import ru.solom.flickrbrowser.core.PhotoForSave
import ru.solom.flickrbrowser.core.file.IFileHandler
import ru.solom.flickrbrowser.core.file.IFileHandlerFactory
import javax.inject.Inject

class FileHandlerFactory @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val fileApi: FileApi
) : IFileHandlerFactory {
    override fun create(photo: PhotoForSave): IFileHandler {
        return FileHandler(activityProvider, fileApi, photo)
    }
}
