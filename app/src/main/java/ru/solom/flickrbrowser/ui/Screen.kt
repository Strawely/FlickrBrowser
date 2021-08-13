package ru.solom.flickrbrowser.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import ru.solom.flickrbrowser.R
import ru.solom.flickrbrowser.interactor.Photo

@Composable
fun Screen(coroutineScope: CoroutineScope) = MaterialTheme {
    val viewModel = LocalViewModel.current!!
    val state = viewModel.state.collectAsState(coroutineScope.coroutineContext)
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
    val viewModel = LocalViewModel.current!!
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