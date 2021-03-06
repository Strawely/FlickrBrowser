package ru.solom.flickrbrowser.main.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import ru.solom.flickrbrowser.core.Photo
import ru.solom.flickrbrowser.main.R
import ru.solom.flickrbrowser.main.util.collectEvent

@Composable
fun Screen() = MaterialTheme {
    val viewModel = LocalViewModel.current
    val lifecycleContext = LocalLifecycleOwner.current.lifecycle.coroutineScope.coroutineContext
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    Box {
        if (state.value.loading) Progress()
        if (state.value.error != null) Error(error = state.value.error!!)
        if (state.value.list != null) ItemsList(items = state.value.list!!)
    }
    LaunchedEffect(key1 = viewModel.fileEvents, key2 = lifecycleContext) {
        viewModel.fileEvents.collectEvent(scope = this) { msgRes ->
            Toast.makeText(context, msgRes, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun ItemsList(items: List<Photo>) {
    LazyColumn {
        items(items) { item -> Item(photo = item) }
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
private fun Item(photo: Photo) {
    val viewModel = LocalViewModel.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { viewModel.onItemClick(photo) },
        elevation = 4.dp
    ) {
        Box {
            ItemContent(photo)
            if (photo.isHighlighted) ItemHighlight()
        }
    }
}

@Composable
private fun BoxScope.ItemHighlight() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(Color.Black.copy(alpha = .5f))
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = rememberImagePainter(data = R.drawable.ic_arrow_downward),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Green)
        )
    }
}

@Composable
private fun ItemContent(photo: Photo) {
    Column(modifier = Modifier.padding(8.dp)) {
        val imageRequest = ImageRequest.Builder(LocalContext.current.applicationContext)
            .data(photo.url)
            .placeholder(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_launcher_background)
            .build()
        Image(
            painter = rememberImagePainter(request = imageRequest),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(text = photo.title, style = MaterialTheme.typography.body2)
    }
}
