package com.blendvision.player.advanced.playback.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.advanced.playback.sample.databinding.ActivityMainBinding
import com.blendvision.player.playback.presentation.callback.DefaultDialogEventListener
import com.blendvision.player.playback.presentation.entity.PanelType
import com.blendvision.player.playback.presentation.entity.SettingOptionConfig
import com.blendvision.player.playback.presentation.menu.DefaultMenuFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    //[optional]
    private var fullscreenButton: View? = null

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
            defaultPanelType = PanelType.EMBEDDED,
            disableControlPanel = null
        )

        customizationPlayerUI()

        viewBinding.playerView.setUnifiedPlayer(mainViewModel.getPlayer())

    }

    //You can refer to the following example to understand how to customize the player UI.
    private fun customizationPlayerUI(){
        // Set default dialog for error handling
        val defaultDialogEventListener =
            DefaultDialogEventListener(this, viewBinding.playerView)
        viewBinding.playerView.setDialogEventListener(defaultDialogEventListener)

        // Create setting dialog
        viewBinding.playerView.setupTopMenuItems(createTopMenuItems())
        viewBinding.playerView.setupBottomMenuItems(createBottomMenuItems())

        // Hide useless options
        viewBinding.playerView.configureSettingOption(
            SettingOptionConfig(
                forceHideAutoPlay = true
            )
        )
    }

    private fun createTopMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(viewBinding.playerView)
        return listOf(
            menuFactory.createInfoMenuItem(),
            menuFactory.createShareMenuItem {
                // Handle share action
            }
        )
    }

    private fun createBottomMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(viewBinding.playerView)
        fullscreenButton = menuFactory.createFullScreenMenuItem { isFullScreen ->
            // TODO callback for onClick event
        }
        return listOf(
            menuFactory.createSettingMenuItem(),
            fullscreenButton!!
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
        mainViewModel.releasePlayer()
        super.onDestroy()
    }
}
