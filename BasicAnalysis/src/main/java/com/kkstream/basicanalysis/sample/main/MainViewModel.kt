package com.kkstream.basicanalysis.sample.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kkstream.playcraft.analytics.presentation.kksplayer_collector.AnalyticsConfig
import com.kkstream.playcraft.paas.player.common.PlayerConfig
import com.kkstream.playcraft.paas.player.common.UniPlayer
import com.kkstream.playcraft.paas.player.common.callback.PlayLogger
import com.kkstream.playcraft.paas.player.common.data.MediaConfig
import com.kkstream.playcraft.paas.player.common.data.PlayerOptions

class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = listOf(
            MediaConfig.Source(
                url = "MPD_URL",
                protocol = MediaConfig.Protocol.DASH,
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = "DRM_SERVER_URL",
                    headers = mapOf("KEY" to "VALUE")
                )
            )
        ),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO"
    )

    private val analyticsConfig = AnalyticsConfig.Builder("YOUR_TOKEN") // token is required
        .setResourceId("I am resource id.")  // optional
        .setUserId("I am user id.")          // optional
        .setSessionId("I am session id.")    // optional
        .setTags(listOf("analytics", "1.0.0")) // optional
        .setCustomData(
            mapOf(
                "custom_parameter" to "I am String",
                "custom_parameter2" to 999
            )
        ) // optional
        .build()

    // Initialize the UniPlayer
    fun initializePlayer(
        context: Context
    ) {
        player = UniPlayer.Builder(
            context = context,
            playerConfig = PlayerConfig(
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {}
                }
            )
        ).setAnalyticsConfig(analyticsConfig)//set analyticsConfig
            .build()

        // Optional settings for the player
        player.setPlayerOptions(
            PlayerOptions(
                isThumbnailSeekingEnabled = true
            )
        )
    }

    // Load and start the UniPlayer
    fun loadPlayer() {
        player.load(mediaConfig)
    }

    fun startPlayer() {
        player.start()
    }

    // Pause the UniPlayer
    fun pausePlayer() {
        player.pause()
    }

    fun playPlayer() {
        player.play()
    }

    // Stop the UniPlayer
    fun stopPlayer() {
        player.stop()
    }

    // Release the UniPlayer resources
    fun releasePlayer() {
        player.release()
    }

    fun getPlayer(): UniPlayer {
        return player
    }
}