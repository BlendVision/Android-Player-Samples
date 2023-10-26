package com.blendvision.player.basic.playback.tvsample

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.blendvision.player.basic.playback.tvsample.databinding.ActivityMainBinding
import com.kkstream.playcraft.paas.player.common.callback.ControlStateEventListener
import com.kkstream.playcraft.paas.player.stb.CustomUniTVFragmentInterface
import com.kkstream.playcraft.paas.player.stb.StateType
import com.kkstream.playcraft.paas.player.stb.UniTvFragment

class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var uniTvFragment: UniTvFragment
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        uniTvFragment = UniTvFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, uniTvFragment)
            .commit()

    }

    private fun setupUniTvFragment() {
        val unPlayer = viewModel.setupPlayer(this)
        uniTvFragment.setup(unPlayer)
        uniTvFragment.setPaasThemeColorStyle(R.style.UpdateCustomizeIconColor)
        uniTvFragment.setControlStateEventListener(object : ControlStateEventListener {
            override fun onControlStateChanged(newState: StateType) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        setupUniTvFragment()
        viewModel.setupMediaConfig()
        viewModel.startPlayer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPlayer()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (isFragmentHandleOnKeyUpEvent(keyCode, event)) {
            // If isFragmentHandleOnKeyUpEvent return true, it means we handle the onKeyUp event.
            // So we return true to tell parent we has handled it.
            true
        } else {
            super.onKeyUp(keyCode, event)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (isFragmentHandleOnKeyDownEvent(keyCode, event)) {
            // If isFragmentHandleOnKeyUpEvent return true, it means we handle the onKeyUp event.
            // So we return true to tell parent we has handled it.
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun isFragmentHandleOnKeyUpEvent(keyCode: Int, event: KeyEvent?): Boolean {
        return (uniTvFragment as? CustomUniTVFragmentInterface)?.onKeyUp(keyCode, event, null)
            ?: false
    }

    private fun isFragmentHandleOnKeyDownEvent(keyCode: Int, event: KeyEvent?): Boolean {
        return (uniTvFragment as? CustomUniTVFragmentInterface)?.onKeyDown(keyCode, event, null)
            ?: false
    }
}