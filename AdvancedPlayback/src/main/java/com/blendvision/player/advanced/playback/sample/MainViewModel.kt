package com.blendvision.player.advanced.playback.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.entity.MediaConfig
import com.blendvision.player.playback.presentation.entity.PlayerConfig
import com.blendvision.player.playback.presentation.entity.PlayerOptions
import com.blendvision.player.playback.presentation.logger.PlayLogger

class MainViewModel : ViewModel() {

    private lateinit var player: UniPlayer

    private val mediaConfig = MediaConfig(
        source = MediaConfig.Source(
                url = MPD_URL,
                protocol = MediaConfig.Protocol.DASH,
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = DRM_LICENSE_URL,
                    headers = mapOf(
                        DRM_HEADER_KEY to DRM_HEADER_VALUE
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

        // Sample MPD_URL: https://d2mxta927rohme.cloudfront.net/376c618f-b27a-4a3d-9457-ad7076ee87e3/vod/4516b544-bba7-4a75-a9fc-a83730d81700/vod/dash.mpd
        private const val MPD_URL = "{YOUR_MPD_URL}"
        private const val THUMBNAIL_SEEKING_URL = ""

        private const val DRM_LICENSE_URL = "https://drm.platform.blendvision.com/api/v3/drm/license"
        private const val DRM_HEADER_KEY = "x-custom-data"

        // Sample TOKEN: eyJhbGciOiJSUzI1NiIsImtpZCI6IjRhNDUyZjE1LTQ2NzktNDg3Zi05NjAxLWU5ZTljNDYxZGM2NyIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9pZCI6ImRhODRmZDM1LWNlZGUtNDBkZS04NTNjLTUxMjAyYTRkOGUyYiIsInJlc291cmNlX3R5cGUiOiJSRVNPVVJDRV9UWVBFX1ZPRF9FVkVOVCIsIm9yZ19pZCI6ImY1NDAyZGYxLThjNjMtNGE2Ny04NDliLTA1ZDcyYjE0NGNhZCIsInRlbmFudF9pZCI6IjM3NmM2MThmLWIyN2EtNGEzZC05NDU3LWFkNzA3NmVlODdlMyIsImRybV9jb25maWciOnsidmVyc2lvbiI6MiwiZmFpcnBsYXkiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWV9LCJwbGF5cmVhZHkiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWUsInJlcXVpcmVkX3ByX3NlY3VyaXR5X2xldmVsIjoyMDAwfSwid2lkZXZpbmUiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWUsInJlcXVpcmVkX2VtZV9zZWN1cml0eV9sZXZlbCI6MX19LCJpc3MiOiJvcmJpdCIsImV4cCI6MTcwMzIxOTM2NCwiaWF0IjoxNzAzMTMyOTY0LCJqdGkiOiJiOGJmMTA3ZC05ZWU3LTQ2YWUtYjJjNS1lOWY4NGRlYmIzNWQifQ.XUrv0LqRwnETOkkiROb8FWY9pXNCVhDMhT2XT19tELm4DICR_BlWaZ6AOR4ChsjLUb_x8PUyKqNkW7NRPVFmEqju3VGKDOTbf0ePOK_P5jdQwvrSRHoGHfmRY4qRwj865O8x-rB_1tW-u3hRsdZRJD6FVd-UQHMxaNeTYrQc16hyBbTC7YQx81XQUhwPsMxZnTxB7QVRrftvMkeh9XmChFePlm6VzvYtRe9DmpEU9kSrDILr5DaD_v7MOdjufufftwQ5LOV_9KPRq-4J2swIgUxinzMLW7afTfXDTVBtJegj8DadSrdNlfw0PbcezKlcrrhmL9W-VKl0LrguiBSQEQ
        private const val TOKEN = "{YOUR_PLAYBACK_TOKEN}"
        private const val DRM_HEADER_VALUE = "token_type=upfront&token_value=$TOKEN"
    }

}