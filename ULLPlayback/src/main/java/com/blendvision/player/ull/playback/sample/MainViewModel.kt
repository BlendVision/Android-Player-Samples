package com.blendvision.player.ull.playback.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.playback.player.common.PlayerConfig
import com.blendvision.player.playback.player.common.UniPlayer
import com.blendvision.player.playback.player.common.callback.PlayLogger
import com.blendvision.player.playback.player.common.data.MediaConfig

class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = listOf(
            MediaConfig.Source(
                url = MPD_URL,
                protocol = MediaConfig.Protocol.DASH
            )
        ),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO",
        // [Notice] Need to set below D3 configuration, or ULL won't work
        features = listOf(MediaConfig.Feature.D3)
    )

    // Initialize the UniPlayer
    fun initializePlayer(
        context: Context
    ) {
        player = UniPlayer.Builder(
            context = context,
            playerConfig = PlayerConfig(
                license = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {

                    }
                }
            )
        ).build()
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