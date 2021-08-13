package ru.solom.flickrbrowser

import android.app.Application
import android.util.Log
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import coil.util.DebugLogger
import coil.util.Logger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader {
            ImageLoader.Builder(applicationContext)
                .availableMemoryPercentage(.25)
                .okHttpClient(
                    okHttpClient.newBuilder()
                        .cache(CoilUtils.createDefaultCache(applicationContext))
                        .build()
                )
                .logger(if(BuildConfig.DEBUG) DebugLogger(level = Log.DEBUG) else null)
                .build()
        }
    }
}