package com.blendvision.player.basic.playback.tvsample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.playback.player.common.PlayerConfig
import com.blendvision.player.playback.player.common.UniPlayer
import com.blendvision.player.playback.player.common.callback.PlayLogger
import com.blendvision.player.playback.player.common.data.MediaConfig
import com.blendvision.player.playback.player.common.data.PlayerOptions

class MainViewModel : ViewModel() {

    private var player: UniPlayer? = null

    private val mediaConfig = MediaConfig(
        source = listOf(
            MediaConfig.Source(
                url = "MPD_URL",
                protocol = MediaConfig.Protocol.DASH
            )
        ),
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
            PlayerConfig(playLogger = object : PlayLogger {
                override fun logEvent(eventName: String, properties: Map<String, Any>) {

                }
            })
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
}
