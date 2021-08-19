package ru.solom.flickr.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.solom.flickr.data.MainInteractor
import ru.solom.flickr.data.network.Api
import ru.solom.flickr.data.network.MainRepository
import ru.solom.flickrbrowser.core.IMainInteractor
import ru.solom.flickrbrowser.core.network.IMainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataNetworkModule {
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
interface AbstractDataNetworkModule {
    @Binds
    @Singleton
    fun bindMainRepository(impl: MainRepository): IMainRepository

    @Binds
    @Singleton
    fun bindMainInteractor(impl: MainInteractor): IMainInteractor
}
