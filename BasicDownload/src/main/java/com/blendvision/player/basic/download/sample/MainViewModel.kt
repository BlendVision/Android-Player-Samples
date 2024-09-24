package com.blendvision.player.basic.download.sample

import androidx.lifecycle.ViewModel
import com.blendvision.player.download.presentation.Downloader
import com.blendvision.player.download.presentation.entity.DownloadConfig
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.entity.OfflineMediaConfig

class MainViewModel(
    private val downloader: Downloader,
    private val player: UniPlayer
) : ViewModel() {

    val downloadStatus = downloader.downloadStatus

    val downloadErrorEvent = downloader.errorEvent

    fun startDownload() {
        downloader.download(
            DownloadConfig(
                mpdUrl = DOWNLOAD_URL,
                drmServerUrl = DRM_SERVER_URL,
                drmRequestHeader = DRM_REQUEST_HEADER
            )
        )
    }

    fun pauseDownload() {
        downloader.pause(
            DownloadConfig(
                mpdUrl = DOWNLOAD_URL
            )
        )
    }

    fun resumeDownload() {
        downloader.resume(
            DownloadConfig(
                mpdUrl = DOWNLOAD_URL
            )
        )
    }

    fun removeDownload() {
        downloader.remove(
            DownloadConfig(
                mpdUrl = DOWNLOAD_URL
            )
        )
    }

    fun startOfflinePlayback() {
        player.load(
            OfflineMediaConfig(
                title = "Download Test",
                mpdUrl = DOWNLOAD_URL
            )
        )
        player.start()
    }

    fun stopOfflinePlayback() {
        player.pause()
        player.stop()
    }

    fun getPlayer(): UniPlayer {
        return player
    }

    override fun onCleared() {
        player.release()
    }

    companion object {
        private const val DOWNLOAD_URL = "{YOUR_MPD_URL}"
        private const val DRM_SERVER_URL = "https://drm.platform-qa.kkstream.io/api/v3/drm/licenseKey"

        private const val HEADER_KEY = "x-custom-data"
        // Sample TOKEN: eyJhbGciOiJSUzI1NiIsImtpZCI6IjRhNDUyZjE1LTQ2NzktNDg3Zi05NjAxLWU5ZTljNDYxZGM2NyIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9pZCI6ImRhODRmZDM1LWNlZGUtNDBkZS04NTNjLTUxMjAyYTRkOGUyYiIsInJlc291cmNlX3R5cGUiOiJSRVNPVVJDRV9UWVBFX1ZPRF9FVkVOVCIsIm9yZ19pZCI6ImY1NDAyZGYxLThjNjMtNGE2Ny04NDliLTA1ZDcyYjE0NGNhZCIsInRlbmFudF9pZCI6IjM3NmM2MThmLWIyN2EtNGEzZC05NDU3LWFkNzA3NmVlODdlMyIsImRybV9jb25maWciOnsidmVyc2lvbiI6MiwiZmFpcnBsYXkiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWV9LCJwbGF5cmVhZHkiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWUsInJlcXVpcmVkX3ByX3NlY3VyaXR5X2xldmVsIjoyMDAwfSwid2lkZXZpbmUiOnsiaGRjcCI6ImhkY3Bfbm9uZSIsIm9ubGluZV9kdXJhdGlvbiI6MjU5MjAwLCJvZmZsaW5lX2R1cmF0aW9uIjoyNTkyMDAsImFsbG93X29mZmxpbmVfcGxheWJhY2siOnRydWUsImFsbG93X2FuYWxvZ19vdXQiOnRydWUsInJlcXVpcmVkX2VtZV9zZWN1cml0eV9sZXZlbCI6MX19LCJpc3MiOiJvcmJpdCIsImV4cCI6MTcwMzIxOTM2NCwiaWF0IjoxNzAzMTMyOTY0LCJqdGkiOiJiOGJmMTA3ZC05ZWU3LTQ2YWUtYjJjNS1lOWY4NGRlYmIzNWQifQ.XUrv0LqRwnETOkkiROb8FWY9pXNCVhDMhT2XT19tELm4DICR_BlWaZ6AOR4ChsjLUb_x8PUyKqNkW7NRPVFmEqju3VGKDOTbf0ePOK_P5jdQwvrSRHoGHfmRY4qRwj865O8x-rB_1tW-u3hRsdZRJD6FVd-UQHMxaNeTYrQc16hyBbTC7YQx81XQUhwPsMxZnTxB7QVRrftvMkeh9XmChFePlm6VzvYtRe9DmpEU9kSrDILr5DaD_v7MOdjufufftwQ5LOV_9KPRq-4J2swIgUxinzMLW7afTfXDTVBtJegj8DadSrdNlfw0PbcezKlcrrhmL9W-VKl0LrguiBSQEQ
        private const val HEADER_VALUE = "{YOUR_PLAYBACK_TOKEN}"
        private val DRM_REQUEST_HEADER = mapOf(HEADER_KEY to HEADER_VALUE)
    }

}