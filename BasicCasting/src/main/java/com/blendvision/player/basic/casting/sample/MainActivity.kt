package com.blendvision.player.basic.casting.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.basic.casting.sample.cast_button.DefaultCastButton
import com.blendvision.player.basic.casting.sample.cast_button.ManualCastButton
import com.blendvision.player.basic.casting.sample.databinding.ActivityMainBinding
import com.blendvision.player.playback.cast.presentation.CaaS
import com.blendvision.player.playback.cast.presentation.callback.CastEventListener
import com.blendvision.player.playback.cast.presentation.entity.CastImage
import com.blendvision.player.playback.cast.presentation.entity.CastMetaData
import com.blendvision.player.playback.cast.presentation.entity.CastOptions
import com.blendvision.player.playback.presentation.entity.MediaConfig
import com.blendvision.player.playback.presentation.entity.PanelType

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
                    contentUrl = mediaConfig.source.url,
                    drmInfo = mediaConfig.source.drm,
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
                namespace: String,
                message: String
            ) {
            }

            override fun onMessageReceiveError(errorMessage: String) {}
            override fun onMetaDataChanged(metadata: CastMetaData) {}
            override fun onRemoteClientPlayerStatusUpdated(playerState: Int) {}
            override fun onStartCasting() {}
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
            defaultPanelType = PanelType.EMBEDDED,
            disableControlPanel = null
        )

        viewBinding.playerView.setupBottomMenuItems(
            listOf(castButton)
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
        data object DEFAULT : CreateCastButtonType()
        data object MANUAL : CreateCastButtonType()
    }

}
