package ru.solom.flickrbrowser

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import ru.solom.flickrbrowser.interactor.Photo
import ru.solom.flickrbrowser.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply { setContent { Screen() } })
        viewModel.update()
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Screen() = MaterialTheme {
        val state = viewModel.state.collectAsState(lifecycleScope.coroutineContext)
        val stateValue = state.value
        Box {
            if (stateValue.loading) Progress()
            if (stateValue.error != null) Error(error = stateValue.error)
            if (stateValue.list != null) ItemsList(items = stateValue.list)
        }
    }

    @Composable
    private fun ItemsList(items: List<Photo>) {
        LazyColumn {
            items(items) { item -> Item(photo = item, { }) }
        }
    }

    @Composable
    fun BoxScope.Error(error: String) {
        Text(
            text = error,
            color = MaterialTheme.colors.error,
            modifier = Modifier.align(Alignment.Center)
        )
    }

    @Composable
    private fun BoxScope.Progress() {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colors.primary
        )
    }


    @Composable
    private fun Item(photo: Photo, onClick: (Photo) -> Unit) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { onClick(photo) },
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                val imageRequest = ImageRequest.Builder(LocalContext.current.applicationContext)
                    .data(photo.url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .fallback(R.drawable.ic_launcher_background)
                    .build()
                Image(
                    painter = rememberImagePainter(
                        request = imageRequest,
                        imageLoader = LocalContext.current.applicationContext.imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .align(CenterHorizontally)
                )
                Text(text = photo.title, style = MaterialTheme.typography.body2)
            }
        }
    }
}