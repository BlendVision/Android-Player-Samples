package com.blendvision.player.advanced.playback.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.playback.player.common.PlayerConfig
import com.blendvision.player.playback.player.common.UniPlayer
import com.blendvision.player.playback.player.common.callback.PlayLogger
import com.blendvision.player.playback.player.common.data.MediaConfig
import com.blendvision.player.playback.player.common.data.PlayerOptions

class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = listOf(
            MediaConfig.Source(
                url = "MPD_URL",
                protocol = MediaConfig.Protocol.DASH,
                //DRM info
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = "DRM_SERVER_URL",
                    headers = mapOf("KEY" to "VALUE")
                )
            )
        ),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        //If you have set thumbnailSeekingUrl and the isThumbnailSeekingEnabled setting in the PlayerOptions object is set to true, the player will enable thumbnail seeking when the seek bar is changed.
        thumbnailSeekingUrl = "THUMBNAIL_SEEKING_URL",
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
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {

                    }
                }
            )
        ).build()

        // Optional settings for the player
        player.setPlayerOptions(
            PlayerOptions(
                //Enabled Thumbnail Seeking
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