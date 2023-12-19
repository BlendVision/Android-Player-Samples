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
                url = MPD_URL,
                protocol = MediaConfig.Protocol.DASH,
                //DRM info
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = DRM_LICENSE_URL,
                    headers = mapOf(
                        DRM_HEADER_KEY to DRM_HEADER_VALUE
                    )
                )
            )
        ),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        //If you have set thumbnailSeekingUrl and the isThumbnailSeekingEnabled setting in the PlayerOptions object is set to true, the player will enable thumbnail seeking when the seek bar is changed.
        thumbnailSeekingUrl = THUMBNAIL_SEEKING_URL,
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
                license = PLAYER_LICENSE,
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

    companion object {
        private const val PLAYER_LICENSE = "{YOUR_PLAYER_LICENSE}"

        private const val MPD_URL = "https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/dea931c3-8766-477d-a87b-1c3f91490139/vod/dash.mpd"
        private const val THUMBNAIL_SEEKING_URL = "https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/dea931c3-8766-477d-a87b-1c3f91490139/thumbnail/00/thumbnails.vtt"

        private const val DRM_LICENSE_URL = "{YOUR_DRM_LICENSE_URL}"
        private const val DRM_HEADER_KEY = "x-custom-data"

        private const val TOKEN = "{YOUR_PLAYBACK_TOKEN}"
        private const val DRM_HEADER_VALUE = "token_type=upfront&token_value=$TOKEN"
    }

}