package com.blendvision.basic.download.factory

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blendvision.basic.download.MainViewModel
import com.blendvision.player.download.Downloader
import com.blendvision.player.download.ui.entity.DownloadTrackSelection
import com.blendvision.player.download.ui.entity.DownloadableTracks
import com.blendvision.player.playback.player.common.PlayerConfig
import com.blendvision.player.playback.player.common.UniPlayer
import com.blendvision.player.playback.player.common.callback.PlayLogger

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
                license = PLAYER_LICENSE,
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {
                        Log.d(TAG, "$eventName, $properties")
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