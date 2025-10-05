package com.example.drishtimukesh.screen

//package com.example.drishtimukesh.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException // Required for specific error handling
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

/**
 * A composable screen that uses ExoPlayer to play a streaming video URL.
 * It provides a full-screen, immersive experience with buffering and error indicators.
 *
 * @param videoUrl The URL of the video stream to play (must be URL-decoded).
 * @param navController The NavController for navigation.
 */
@Composable
fun VideoPlayerScreen(videoUrl: String, navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current // FIX: Corrected typo 'LocalLifecycleLifecycleOwner'

    // State to track the player state for buffering/loading indication
    var playerState by remember { mutableIntStateOf(Player.STATE_IDLE) }

    // NEW STATE: Explicitly track if an unrecoverable error has occurred
    var isError by remember { mutableStateOf(false) }

    // 1. Initialize ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true

            // Attach a listener to update the player state and detect errors
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    playerState = state
                }

                // FIX: Explicitly capture and report playback errors using the dedicated callback
                override fun onPlayerError(error: PlaybackException) {
                    isError = true
                }
            })
        }
    }

    // 2. Lifecycle Management (for pausing/resuming video playback)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Release the player when the composable leaves the screen
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    // 3. UI: Full-screen video view with loading and error indicators
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Pure black for immersive viewing
        contentAlignment = Alignment.Center
    ) {
        // ExoPlayer View (integrated via AndroidView)
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true // Show standard controls (play/pause, seek)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Error Message Overlay (Highest priority UI)
        if (isError) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)) // Semi-transparent overlay to focus on error
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Video Error Icon",
                    tint = MaterialTheme.colorScheme.error, // Use Material error color (typically red)
                    modifier = Modifier.size(72.dp) // Larger icon
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Video Unavailable",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "We could not load this video. Please check the link or try a different lecture.",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Loading Indicator (Shown only if not in error state and is loading)
        else if (playerState == Player.STATE_BUFFERING || playerState == Player.STATE_IDLE) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp), // Increased size for visibility
                color = MaterialTheme.colorScheme.primary, // High-visibility color
                strokeWidth = 6.dp
            )
        }
    }
}