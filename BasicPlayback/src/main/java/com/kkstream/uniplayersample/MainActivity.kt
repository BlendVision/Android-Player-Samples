package com.kkstream.uniplayersample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kkstream.playcraft.paas.player.common.ContentType
import com.kkstream.playcraft.paas.player.common.PlayerConfig
import com.kkstream.playcraft.paas.player.common.UniPlayer
import com.kkstream.playcraft.paas.player.common.callback.PlayLogger
import com.kkstream.playcraft.paas.player.common.data.MediaConfig
import com.kkstream.playcraft.paas.player.common.data.PlayerOptions
import com.kkstream.playcraft.paas.player.common.data.SettingOptionConfig
import com.kkstream.playcraft.paas.player.mobile.DefaultDialogEventListener
import com.kkstream.playcraft.paas.player.mobile.DefaultMenuFactory
import com.kkstream.uniplayersample.databinding.ActivityMainBinding
import com.kkstream.uniplayersample.utils.ScreenUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var player: UniPlayer? = null

    //[optional]
    private var fullscreenButton: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adjustPlayerViewHeight()

        // Setup the type of control panel
        binding.kksPlayerServiceView.setupControlPanel(
            autoKeepScreenOnEnabled = true,
            defaultContentType = ContentType.EMBEDDED
        )

        // Creating the player and attach it to player view
        player = UniPlayer.Builder(
            context = this,
            // PlayerConfig can set the license key with ${license} parameter, or you can also set it in AndroidManifest.
            // The default priority is PlayerConfig first, AndroidManifest second.
            // note: if you didn't set the correct license key, you will get the 20403 error.
            playerConfig = PlayerConfig(
                playLogger = object : PlayLogger {
                    override fun logEvent(eventName: String, properties: Map<String, Any>) {
                        // do something
                    }
                }
            )
        ).build()
        binding.kksPlayerServiceView.setUnifiedPlayer(player)

        // player optional setting
        player?.setPlayerOptions(
            PlayerOptions(
                isThumbnailSeekingEnabled = true
            )
        )

        // Set default dialog for error handling
        val defaultDialogEventListener = DefaultDialogEventListener(this, binding.kksPlayerServiceView)
        binding.kksPlayerServiceView.setDialogEventListener(defaultDialogEventListener)

        // create setting dialog
        binding.kksPlayerServiceView.setupTopMenuItems(createTopMenuItems())
        binding.kksPlayerServiceView.setupBottomMenuItems(createBottomMenuItems())

        // hide useless option
        configSettingDialog()
    }

    private fun adjustPlayerViewHeight() {
        val screenWidth = ScreenUtil.getScreenWidth(this)
        val calculatedHeight = screenWidth * 16 / 9
        binding.kksPlayerServiceView.layoutParams.height = ScreenUtil.px2dp(this, calculatedHeight)
    }

    private fun configSettingDialog() {
        binding.kksPlayerServiceView.configureSettingOption(
            SettingOptionConfig(
                forceHideAutoPlay = true
            )
        )
    }

    private fun createTopMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(binding.kksPlayerServiceView)
        return listOf(
            menuFactory.createInfoMenuItem(),
            menuFactory.createShareMenuItem {
                // do something
            }
        )
    }

    private fun createBottomMenuItems(): List<View> {
        val menuFactory = DefaultMenuFactory(binding.kksPlayerServiceView)
        fullscreenButton = menuFactory.createFullScreenMenuItem { isFullScreen ->
            // TODO callback for onClick event
        }
        return listOf(
            menuFactory.createSettingMenuItem(),
            fullscreenButton!!
        )
    }

    private fun provideMediaConfig() = MediaConfig(
        source = listOf(
            MediaConfig.Source(
                url = "MPD_URL",
                protocol = MediaConfig.Protocol.DASH,
                // If you want to playback a DRM protected content, should set as below; others set it to null
                drm = MediaConfig.DrmInfo.Widevine(
                    licenseUrl = "DRM_SERVER_URL",
                    headers = mapOf("KEY" to "VALUE")
                )
            )
        ),
        title = "CONTENT_TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO"
    )

    override fun onStart() {
        super.onStart()
        player?.load(provideMediaConfig())
        player?.start()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
    }
}