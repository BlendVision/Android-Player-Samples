package com.kkstream.basiccasting.sample.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kkstream.playcraft.paas.player.common.PlayerConfig
import com.kkstream.playcraft.paas.player.common.UniPlayer
import com.kkstream.playcraft.paas.player.common.callback.PlayLogger
import com.kkstream.playcraft.paas.player.common.data.MediaConfig
import com.kkstream.playcraft.paas.player.common.data.PlayerOptions

class MainViewModel : ViewModel() {


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

    private lateinit var player: UniPlayer

    // Initialize the UniPlayer
    fun initializePlayer(context: Context) {
        player = UniPlayer.Builder(
            context = context,
            playerConfig = PlayerConfig(
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {

                    }
                }
            )
        ).build()

        // Optional settings for the player
        player.setPlayerOptions(
            PlayerOptions(
                isThumbnailSeekingEnabled = true
            )
        )
    }

    fun loadPlayer() {
        player.load(mediaConfig)
    }

    fun startPlayer() {
        player.start()
    }

    fun playPlayer() {
        player.play()
    }

    // Pause the UniPlayer
    fun pausePlayer() {
        player.pause()
    }

    // Stop the UniPlayer
    fun stopPlayer() {
        player.stop()
    }

    // Release the UniPlayer resources
    fun releasePlayer() {
        player.release()
    }

    fun getMediaConfig(): MediaConfig {
        return mediaConfig
    }

    fun getPlayer(): UniPlayer {
        return player
    }
}
