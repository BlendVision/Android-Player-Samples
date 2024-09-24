package com.blendvision.player.basic.audio.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.common.presentation.entity.log.LogEvent
import com.blendvision.player.common.presentation.entity.log.LogProperty
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.entity.MediaConfig
import com.blendvision.player.playback.presentation.entity.PlayerConfig
import com.blendvision.player.playback.presentation.logger.PlayLogger

class MainViewModel:ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = MediaConfig.Source(
                url = MPD_URL,
                protocol = MediaConfig.Protocol.DASH,
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = "DRM_SERVER_URL",
                    headers = mapOf()
                )
        ),
        title = "I'm playing audio",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO"
    )

    // Initialize the UniPlayer
    fun initializePlayer(
        context: Context
    ) {
        player = UniPlayer.Builder(
            context = context,
            playerConfig = PlayerConfig(
                licenseKey = PLAYER_LICENSE,
                isLoopEnabled = true,
                playLogger = object : PlayLogger {
                    override fun onLogEvent(logEvent: LogEvent, properties: Map<LogProperty, Any>) {

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