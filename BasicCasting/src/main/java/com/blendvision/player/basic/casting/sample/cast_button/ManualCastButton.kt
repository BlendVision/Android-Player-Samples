package com.blendvision.player.basic.casting.sample.cast_button

import android.view.LayoutInflater
import android.view.View
import androidx.mediarouter.app.MediaRouteButton
import com.blendvision.player.basic.casting.sample.R
import com.blendvision.player.basic.casting.sample.databinding.ActivityMainBinding
import com.blendvision.player.playback.cast.presentation.CaaS

class ManualCastButton(private val binding: ActivityMainBinding) : CastButton {
    override fun provideCastButton(): View {
        val context = binding.root.context
        val inflater = LayoutInflater.from(context)
        val mediaRouteButton = inflater.inflate(
            R.layout.button_cast,
            binding.playerView,
            false
        ) as MediaRouteButton
        CaaS.setup(context, mediaRouteButton)
        return mediaRouteButton
    }
}
