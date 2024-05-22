package com.blendvision.player.basic.audio.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.basic.audio.sample.databinding.ActivityMainBinding
import com.blendvision.player.playback.presentation.callback.DefaultDialogEventListener
import com.blendvision.player.playback.presentation.entity.PanelType
import com.blendvision.player.playback.presentation.entity.SettingOptionConfig
import com.blendvision.player.playback.presentation.menu.DefaultMenuFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.initializePlayer(this)

        configurePlayer()

    }

    private fun configurePlayer() {

        // Setup the type of control panel
        binding.playerView.setupControlPanel(
            autoKeepScreenOnEnabled = true,
            defaultPanelType = PanelType.EMBEDDED,
            disableControlPanel = null
        )

        binding.playerView.enablePlaybackControlsAuto(false)

        binding.playerView.configureSettingOption(SettingOptionConfig(forceHideAutoPlay = true))

        binding.playerView.setPoster(url = RECEIVER_BACKGROUND_IMAGE)

        binding.playerView.setUnifiedPlayer(mainViewModel.getPlayer())

        customizationPlayerUI()

    }

    //You can refer to the following example to understand how to customize the player UI.
    private fun customizationPlayerUI() {
        // Set default dialog for error handling
        val defaultDialogEventListener =
            DefaultDialogEventListener(this, binding.playerView)
        binding.playerView.setDialogEventListener(defaultDialogEventListener)

        // Create setting dialog
        binding.playerView.setupTopMenuItems(createTopMenuItems())
        binding.playerView.setupBottomMenuItems(createBottomMenuItems())

    }

    private fun createTopMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(binding.playerView)
        return listOf(
            menuFactory.createInfoMenuItem(),
            menuFactory.createShareMenuItem {}
        )
    }

    private fun createBottomMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(binding.playerView)
        return listOf(
            menuFactory.createMuteMenuItem(),
            menuFactory.createSettingMenuItem()
        )
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
        super.onDestroy()
        mainViewModel.releasePlayer()
    }

    companion object {

        private const val RECEIVER_BACKGROUND_IMAGE =
            "https://www.pngmart.com/files/2/Pikachu-PNG-Transparent-Image.png"
    }

}