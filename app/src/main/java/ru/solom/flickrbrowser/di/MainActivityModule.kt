package ru.solom.flickrbrowser.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.solom.flickrbrowser.interactor.IMainInteractor
import ru.solom.flickrbrowser.interactor.MainInteractor

@Module
@InstallIn(SingletonComponent::class)
interface MainActivityModule {
    @Binds
    fun bindMainInteractor(impl: MainInteractor): IMainInteractor
}