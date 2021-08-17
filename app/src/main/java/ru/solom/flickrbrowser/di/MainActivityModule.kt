package ru.solom.flickrbrowser.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.solom.flickrbrowser.core.ActivityProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainActivityModule {
    @Provides
    @Singleton
    fun bindActivityProvider(@ApplicationContext app: Context): ActivityProvider {
        return app as ActivityProvider
    }
}
