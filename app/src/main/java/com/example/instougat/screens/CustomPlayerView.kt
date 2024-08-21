package com.example.instougat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.network.models.response.domain.Post

@Composable
fun CustomPlayerView(modifier: Modifier = Modifier, videoUrl: String) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var playWhenReady by rememberSaveable { mutableStateOf(true) }
    val player = remember {
        ExoPlayer.Builder(context).build().apply {

            setMediaItem(MediaItem.fromUri(videoUrl))

        }
    }

    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    playWhenReady = false
                    player.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    playWhenReady = true
                    player.playWhenReady = true
                }
                Lifecycle.Event.ON_STOP -> {
                    player.stop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        factory = {
            PlayerView(context).apply {
                this.player = player
                this.useController = true // Display the default controls
                this.setBackgroundColor(android.graphics.Color.BLACK) // Set background color to black
            }
        },
        update = {
            it.player = player
        }
    )
}
