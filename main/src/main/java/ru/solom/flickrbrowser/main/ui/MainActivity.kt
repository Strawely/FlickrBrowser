package ru.solom.flickrbrowser.main.ui

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.ComposeView
import dagger.hilt.android.AndroidEntryPoint
import ru.solom.flickrbrowser.core.ActivityProvider
import ru.solom.flickrbrowser.main.ImageFileActivityContract
import ru.solom.flickrbrowser.main.viewmodel.MainViewModel

interface FileActivity {
    val fileCreateLauncher: ActivityResultLauncher<String>?
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FileActivity {
    private val viewModel: MainViewModel by viewModels()
    override var fileCreateLauncher: ActivityResultLauncher<String>? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ActivityProvider).currentActivity = this
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                CompositionLocalProvider(LocalViewModel provides viewModel) {
                    Screen()
                }
            }
        })
        fileCreateLauncher = registerForActivityResult(ImageFileActivityContract()) { uri ->
            viewModel.onPhotoFileCreated(uri)
        }
        viewModel.update()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            (application as ActivityProvider).currentActivity = null
        }
    }
}

/**
 * Use to provide [MainViewModel] instance onto [@Composable] functions.
 * You always have to provide proper instance of [MainViewModel]
 *
 * @exception IllegalStateException when value wasn't provided
 */
internal val LocalViewModel =
    compositionLocalOf<MainViewModel> { error("MainViewModel was not initialized") }
