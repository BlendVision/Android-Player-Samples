package com.blendvision.player.basic.casting.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.basic.casting.sample.cast_button.DefaultCastButton
import com.blendvision.player.basic.casting.sample.cast_button.ManualCastButton
import com.blendvision.player.basic.casting.sample.databinding.ActivityMainBinding
import com.blendvision.player.playback.cast.player.CaaS
import com.blendvision.player.playback.cast.player.data.CastImage
import com.blendvision.player.playback.cast.player.data.CastMetaData
import com.blendvision.player.playback.cast.player.data.CastOptions
import com.blendvision.player.playback.cast.player.listener.CastEventListener
import com.blendvision.player.playback.player.common.PanelType
import com.blendvision.player.playback.player.common.data.MediaConfig
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.MediaQueueItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var castButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Initialize the UniPlayer within ViewModel
        mainViewModel.initializePlayer(context = this)

        configurePlayer()

        setupCaaS(mainViewModel.getMediaConfig())

    }

    private fun createCastButton(createCastButtonType: CreateCastButtonType): View {
        return when (createCastButtonType) {
            is CreateCastButtonType.DEFAULT -> DefaultCastButton(viewBinding).provideCastButton()
            is CreateCastButtonType.MANUAL -> ManualCastButton(viewBinding).provideCastButton()
        }
    }

    private fun setupCaaS(mediaConfig: MediaConfig) {
        CaaS.addListener(object : CastEventListener {
            override fun onConnected() {
                val castOptions = CastOptions(
                    title = mediaConfig.title,
                    contentUrl = mediaConfig.source[0].url,
                    drmInfo = mediaConfig.source[0].drm,
                    images = listOf(
                        CastImage(
                            url = mediaConfig.imageUrl
                        )
                    )
                )
                CaaS.cast(castOptions)
            }

            override fun onDisconnected() {}
            override fun onMessageReceive(
                castDevice: CastDevice,
                namespace: String,
                message: String
            ) {
            }

            override fun onMessageReceiveError(errorMessage: String) {}
            override fun onMetaDataChanged(metadata: CastMetaData) {}
            override fun onRemoteClientPlayerStatusUpdated(playerState: Int) {}
            override fun onStartCasting(media: MediaQueueItem) {}
            override fun onStopCasting() {}
        })
    }

    private fun configurePlayer() {

        /**
         * CreateCastButtonType:
         *  CreateCastButtonType.DEFAULT: Provided through SDK
         *  CreateCastButtonType.MANUAL: customize cast button
         */
        castButton = createCastButton(CreateCastButtonType.MANUAL)

        // Setup the type of control panel
        viewBinding.playerView.setupControlPanel(
            autoKeepScreenOnEnabled = true,
            defaultPanelType = PanelType.EMBEDDED
        )


        viewBinding.playerView.setUnifiedPlayer(mainViewModel.getPlayer())

    }

    override fun onStart() {
        super.onStart()
        mainViewModel.loadPlayer()
        mainViewModel.startPlayer()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.playPlayer()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.stopPlayer()
    }

    override fun onDestroy() {
        mainViewModel.releasePlayer()
        super.onDestroy()
    }

    sealed class CreateCastButtonType {
        object DEFAULT : CreateCastButtonType()
        object MANUAL : CreateCastButtonType()
    }

}
