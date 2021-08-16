package ru.solom.flickrbrowser.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.solom.flickrbrowser.core.file.IFileHandlerFactory
import ru.solom.flickrbrowser.file.FileApi
import ru.solom.flickrbrowser.file.FileHandlerFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractFileModule {
    @Binds
    fun bindFileHandlerFactory(impl: FileHandlerFactory): IFileHandlerFactory
}

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    @Singleton
    fun provideFileApi(retrofit: Retrofit): FileApi = retrofit.create(FileApi::class.java)
}
