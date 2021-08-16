package ru.solom.flickrbrowser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.solom.flickrbrowser.BuildConfig
import ru.solom.flickrbrowser.core.network.FlickrBuildInfo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildInfoModule {
    @Provides
    @Singleton
    fun provideFlickrBuildInfo() = object : FlickrBuildInfo {
        override val apiKey = BuildConfig.API_KEY
    }
}
