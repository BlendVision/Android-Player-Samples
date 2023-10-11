package com.kkstream.basiccasting.sample.main.cast_button

import android.view.View
import com.kkstream.basiccasting.sample.databinding.ActivityMainBinding
import com.kkstream.playcraft.paas.player.mobile.DefaultMenuFactory

class DefaultCastButton(private val binding: ActivityMainBinding) : CastButton {
    override fun provideCastButton(): View {
        val menuFactory = DefaultMenuFactory(binding.kksPlayerServiceView)
        return menuFactory.createCastMenuItem()
    }
}