package ru.solom.flickrbrowser

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import ru.solom.flickrbrowser.core.ActivityProvider
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ActivityProvider {
    @Inject
    lateinit var okHttpClient: OkHttpClient

    override var currentActivity: AppCompatActivity? = null

    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader {
            ImageLoader.Builder(applicationContext)
                .availableMemoryPercentage(COIL_MEMORY_CACHE_PERCENT)
                .okHttpClient(
                    okHttpClient.newBuilder()
                        .cache(CoilUtils.createDefaultCache(applicationContext))
                        .build()
                )
                .logger(if (BuildConfig.DEBUG) DebugLogger(level = Log.DEBUG) else null)
                .build()
        }
    }

    override fun requireActivity() = currentActivity!!
}

private const val COIL_MEMORY_CACHE_PERCENT = .25
