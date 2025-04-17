package com.blendvision.player.basic.download.sample.factory

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.basic.download.sample.MainViewModel
import com.blendvision.player.common.presentation.model.log.LogEvent
import com.blendvision.player.common.presentation.model.log.LogProperty
import com.blendvision.player.download.presentation.Downloader
import com.blendvision.player.download.presentation.model.DownloadTrackSelection
import com.blendvision.player.download.presentation.model.DownloadableTracks
import com.blendvision.player.playback.presentation.UniPlayer
import com.blendvision.player.playback.presentation.logger.PlayLogger
import com.blendvision.player.playback.presentation.model.PlayerConfig

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val context: Context,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val downloader = Downloader.Builder(context)
            .setTrackSelectorCallback(
                object : DownloadTrackSelection.Callback {
                    override suspend fun selectTrack(downloadableTracks: DownloadableTracks): DownloadTrackSelection {
                        return DownloadTrackSelection(
                            downloadableTracks.videoTracks[0],
                            downloadableTracks.audioTracks[0]
                        )
                    }
                }
            )
            .build()

        val player = UniPlayer.Builder(
            context,
            PlayerConfig(
                licenseKey = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun onLogEvent(logEvent: LogEvent, properties: Map<LogProperty, Any>) {
                        Log.d(TAG, "${logEvent.name}, ${properties.mapKeys { it.key.name }}")
                    }
                }
            )
        ).build()

        return MainViewModel(downloader, player) as T
    }

    companion object {
        private const val TAG = "Offline UniPlayer"
        private const val PLAYER_LICENSE = "{YOUR_PLAYER_LICENSE}"
    }

}