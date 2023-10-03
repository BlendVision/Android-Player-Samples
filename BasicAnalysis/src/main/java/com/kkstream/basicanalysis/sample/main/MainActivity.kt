package com.kkstream.basicanalysis.sample.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kkstream.basicanalysis.sample.databinding.ActivityMainBinding
import com.kkstream.playcraft.paas.player.common.ContentType
import com.kkstream.playcraft.paas.player.common.data.SettingOptionConfig
import com.kkstream.playcraft.paas.player.mobile.DefaultDialogEventListener
import com.kkstream.playcraft.paas.player.mobile.DefaultMenuFactory

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
        viewBinding.kksPlayerServiceView.setupControlPanel(
            autoKeepScreenOnEnabled = true,
            defaultContentType = ContentType.EMBEDDED
        )

        // Set default dialog for error handling
        val defaultDialogEventListener =
            DefaultDialogEventListener(this, viewBinding.kksPlayerServiceView)
        viewBinding.kksPlayerServiceView.setDialogEventListener(defaultDialogEventListener)

        // Create setting dialog
        viewBinding.kksPlayerServiceView.setupTopMenuItems(createTopMenuItems())
        viewBinding.kksPlayerServiceView.setupBottomMenuItems(createBottomMenuItems())

        // Hide useless options
        viewBinding.kksPlayerServiceView.configureSettingOption(
            SettingOptionConfig(
                forceHideAutoPlay = true
            )
        )

        viewBinding.kksPlayerServiceView.setUnifiedPlayer(mainViewModel.getPlayer())

    }

    private fun createTopMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(viewBinding.kksPlayerServiceView)
        return listOf(
            menuFactory.createInfoMenuItem(),
            menuFactory.createShareMenuItem {
                // Handle share action
            }
        )
    }

    private fun createBottomMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(viewBinding.kksPlayerServiceView)
        return listOf(
            menuFactory.createSettingMenuItem(),
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