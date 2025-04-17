package com.blendvision.player.basic.playback.tvsample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.common.presentation.model.log.LogEvent
import com.blendvision.player.common.presentation.model.log.LogProperty
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.logger.PlayLogger
import com.blendvision.player.playback.presentation.model.MediaConfig
import com.blendvision.player.playback.presentation.model.PlayerConfig
import com.blendvision.player.playback.presentation.model.PlayerOptions

class MainViewModel : ViewModel() {

    private var player: UniPlayer? = null

    private val mediaConfig = MediaConfig(
        source = MediaConfig.Source(url = MPD_URL, protocol = MediaConfig.Protocol.DASH),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO"
    )

    fun setupPlayer(context: Context): UniPlayer {
        player = UniPlayer.Builder(
            context,
            PlayerConfig(
                licenseKey = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun onLogEvent(logEvent: LogEvent, properties: Map<LogProperty, Any>) {

                    }
                }
            )
        ).build().apply {
            this.setPlayerOptions(
                PlayerOptions(
                    isThumbnailSeekingEnabled = true
                )
            )
        }
        return player!!
    }

    fun setupMediaConfig() {
        player?.load(mediaConfig)
    }

    fun startPlayer() {
        player?.start()
    }

    fun pausePlayer() {
        player?.pause()
    }

    fun stopPlayer() {
        player?.stop()
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
    }

    companion object {
        private const val PLAYER_LICENSE = "{YOUR_PLAYER_LICENSE}"

        // Sample MPD_URL: https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/dea931c3-8766-477d-a87b-1c3f91490139/vod/dash.mpd
        private const val MPD_URL = "{YOUR_MPD_URL}"
    }

}
