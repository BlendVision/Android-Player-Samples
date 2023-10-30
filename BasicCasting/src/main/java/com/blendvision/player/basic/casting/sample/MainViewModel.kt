package com.blendvision.player.basic.casting.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kkstream.playcraft.paas.player.common.PlayerConfig
import com.kkstream.playcraft.paas.player.common.UniPlayer
import com.kkstream.playcraft.paas.player.common.callback.PlayLogger
import com.kkstream.playcraft.paas.player.common.data.MediaConfig

class MainViewModel : ViewModel() {


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
