package ru.solom.flickr.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.solom.flickr.data.file.FileApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    @Singleton
    fun provideFileApi(retrofit: Retrofit): FileApi = retrofit.create(FileApi::class.java)
}
