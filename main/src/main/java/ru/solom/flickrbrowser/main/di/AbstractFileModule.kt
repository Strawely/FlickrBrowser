package ru.solom.flickrbrowser.main.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.solom.flickrbrowser.core.file.IFileHandlerFactory
import ru.solom.flickrbrowser.main.file.FileHandlerFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractFileModule {
    @Binds
    @Singleton
    fun bindFileHandlerFactory(impl: FileHandlerFactory): IFileHandlerFactory
}
