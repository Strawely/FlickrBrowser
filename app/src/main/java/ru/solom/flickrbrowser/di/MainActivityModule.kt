package ru.solom.flickrbrowser.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.solom.flickrbrowser.ActivityProvider
import ru.solom.flickrbrowser.interactor.IMainInteractor
import ru.solom.flickrbrowser.interactor.MainInteractor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractMainActivityModule {
    @Binds
    @Singleton
    fun bindMainInteractor(impl: MainInteractor): IMainInteractor
}

@Module
@InstallIn(SingletonComponent::class)
object MainActivityModule {
    @Provides
    @Singleton
    fun bindActivityProvider(@ApplicationContext app: Context): ActivityProvider {
        return app as ActivityProvider
    }
}
