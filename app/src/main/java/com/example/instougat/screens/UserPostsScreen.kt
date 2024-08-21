package com.example.instougat.screens

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.instougat.screens.accounts.AccountsViewModel
import com.example.network.models.response.domain.Post

@Composable
fun UserPostsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    navController: NavController,
    id: String
) {
    val res by viewModel.res
    val loading by viewModel.loading

    LaunchedEffect(id) {
        viewModel.id = id
        viewModel.getRes()
    }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            columns = StaggeredGridCells.Fixed(2)
        ) {
            itemsIndexed(res) { index, item ->
                PostItem(item = item, onClick = {
                    navController.navigate(VideoScreenObject(item.url))
                })
                if (!loading && index >= res.size - 4) {
                    viewModel.getRes()
                }
            }
        }

        if (loading) {
            Text(text = "loading...")
        }
    }
}

@Composable
fun PostItem(item: Post, onClick: () -> Unit) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .aspectRatio(item.aspectRatio),
            onClick = onClick
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.displayUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image"
            )
        }

        val likes = when {
            item.likes >= 1_000_000 -> "${item.likes / 1_000_000}M"
            item.likes >= 1_000 -> "${item.likes / 1_000}K"
            else -> item.likes.toString()
        }

        Text(
            text = "${item.urlType.name} $likes",
            Modifier
                .padding(8.dp)
                .align(Alignment.End)
        )
    }
}
