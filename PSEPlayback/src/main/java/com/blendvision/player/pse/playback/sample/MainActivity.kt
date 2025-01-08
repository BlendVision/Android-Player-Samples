package com.blendvision.player.pse.playback.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.playback.presentation.callback.DefaultDialogEventListener
import com.blendvision.player.playback.presentation.entity.PanelType
import com.blendvision.player.pse.playback.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Initialize the UniPlayer within ViewModel
        mainViewModel.initializePlayer(context = this)

        configurePlayer()

    }

    private fun configurePlayer() {

        // Setup the type of control panel
        viewBinding.playerView.setupControlPanel(
            autoKeepScreenOnEnabled = true,
            defaultPanelType = PanelType.EMBEDDED
        )

        // [Optional] Setup the error popup dialog if needed
        val defaultDialogEventListener = DefaultDialogEventListener(this, viewBinding.playerView)
        viewBinding.playerView.setDialogEventListener(defaultDialogEventListener)

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

}