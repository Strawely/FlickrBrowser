package ru.solom.flickrbrowser.core.file

import ru.solom.flickrbrowser.core.PhotoForSave

interface IFileHandlerFactory {
    fun create(photo: PhotoForSave): IFileHandler
}
