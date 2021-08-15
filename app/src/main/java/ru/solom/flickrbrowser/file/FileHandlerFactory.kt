package ru.solom.flickrbrowser.file

import ru.solom.flickrbrowser.ActivityProvider
import javax.inject.Inject

interface IFileHandlerFactory {
    fun create(photo: PhotoForSave): IFileHandler
}

class FileHandlerFactory @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val fileApi: FileApi
) : IFileHandlerFactory {
    override fun create(photo: PhotoForSave): IFileHandler {
        return FileHandler(activityProvider, fileApi, photo)
    }
}
