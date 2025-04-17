package com.blendvision.player.basic.download.sample

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blendvision.player.basic.download.sample.databinding.ActivityMainBinding
import com.blendvision.player.basic.download.sample.factory.MainViewModelFactory
import com.blendvision.player.playback.presentation.callback.DefaultDialogEventListener
import com.blendvision.player.playback.presentation.menu.DefaultMenuFactory
import com.blendvision.player.playback.presentation.model.PanelType
import com.blendvision.player.playback.presentation.model.SettingOptionConfig
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    //[optional]
    private var fullscreenButton: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setup()
        requestPermission()
        configurePlayerView()
        registerListeners()
    }

    private fun setup() {
        val factory = MainViewModelFactory(applicationContext)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATION_PERMISSION_REQUEST_CODE )
        }
    }

    private fun configurePlayerView() {

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

    private fun registerListeners() {
        viewBinding.btnStartDownload.setOnClickListener {
            mainViewModel.startDownload()
        }

        viewBinding.btnPauseDownload.setOnClickListener {
            mainViewModel.pauseDownload()
        }

        viewBinding.btnResumeDownload.setOnClickListener {
            mainViewModel.resumeDownload()
        }

        viewBinding.btnRemoveDownload.setOnClickListener {
            mainViewModel.removeDownload()
        }

        viewBinding.btnStartPlayback.setOnClickListener {
            mainViewModel.startOfflinePlayback()
        }

        mainViewModel.downloadStatus.onEach {
            if (it.isEmpty()) {
                viewBinding.progressIndicator.progress = 0
                return@onEach
            }

            viewBinding.progressIndicator.progress = it[0].percentDownloaded.toInt()
        }.launchIn(lifecycleScope)

        mainViewModel.downloadErrorEvent.onEach {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }.launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()

        mainViewModel.stopOfflinePlayback()
    }

    companion object {
        private const val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 9999
    }
}