package com.blendvision.player.pse.playback.sample

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.blendvision.player.common.presentation.model.log.LogEvent
import com.blendvision.player.common.presentation.model.log.LogProperty
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.logger.PlayLogger
import com.blendvision.player.playback.presentation.model.MediaConfig
import com.blendvision.player.playback.presentation.model.PlayerConfig

class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        // PSE only support non-DRM content, please keep `drm` field as null
        source = MediaConfig.Source(url = MPD_URL, protocol = MediaConfig.Protocol.DASH),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO",
        // [Notice] Need to set below PSE configuration, or PSE won't work
        features = listOf(MediaConfig.Feature.PSE),
        /**
         * PSE mode
         * - Standard means the quality will upgrade to next level of 720p, and bitrate keep as same as 720p
         * - Saving means the quality will almost the same as 720p, but the bitrate downgrade to next level
         *
         * Default setting is STANDARD
         */
        pseMode = MediaConfig.PSEMode.STANDARD
    )

    // Initialize the UniPlayer
    fun initializePlayer(
        context: Context
    ) {
        player = UniPlayer.Builder(
            context = context,
            playerConfig = PlayerConfig(
                licenseKey = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun onLogEvent(logEvent: LogEvent, properties: Map<LogProperty, Any>) {
                        /**
                         * Developers can notice the PSE status in log properties after `playback_video_began` happened
                         * `is_pse_enabled`: true means PSE is enabled, otherwise false.
                         */
                        Log.d(TAG, "${logEvent.name}, ${properties.mapKeys { it.key.name }}")
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

    fun setQuality() {
        val result = player.setQuality(height = 480)
        if (result.not()) {
            Log.d(TAG, "Can't change quality when PSE enabled")
        }
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
        private const val TAG = "PSEPlayback"
        private const val PLAYER_LICENSE = "{YOUR_PLAYER_LICENSE}"

        // Sample MPD_URL: https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/dea931c3-8766-477d-a87b-1c3f91490139/vod/dash.mpd
        private const val MPD_URL = "{YOUR_MPD_URL}"
    }

}