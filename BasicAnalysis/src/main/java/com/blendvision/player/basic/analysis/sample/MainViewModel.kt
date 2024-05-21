package com.blendvision.player.basic.analysis.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.analytics.presentation.main.AnalyticsConfig
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.entity.MediaConfig
import com.blendvision.player.playback.presentation.entity.PlayerConfig
import com.blendvision.player.playback.presentation.logger.PlayLogger


class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = MediaConfig.Source(url = MPD_URL, protocol = MediaConfig.Protocol.DASH),
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
                license = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {}
                }
            )
        ).setAnalyticsConfig(analyticsConfig)//set analyticsConfig
            .build()

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

    companion object {
        private const val PLAYER_LICENSE = "{YOUR_PLAYER_LICENSE}"

        // Sample MPD_URL: https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/dea931c3-8766-477d-a87b-1c3f91490139/vod/dash.mpd
        private const val MPD_URL = "{YOUR_MPD_URL}"
    }

}