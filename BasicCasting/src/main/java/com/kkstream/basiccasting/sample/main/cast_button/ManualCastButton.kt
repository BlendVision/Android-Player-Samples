package com.kkstream.basiccasting.sample.main.cast_button

import android.view.LayoutInflater
import android.view.View
import androidx.mediarouter.app.MediaRouteButton
import com.kkstream.basiccasting.sample.R
import com.kkstream.basiccasting.sample.databinding.ActivityMainBinding
import com.kkstream.playcraft.caas.player.CaaS

class ManualCastButton(private val binding: ActivityMainBinding) : CastButton {
    override fun provideCastButton(): View {
        val context = binding.root.context
        val inflater = LayoutInflater.from(context)
        val mediaRouteButton = inflater.inflate(
            R.layout.button_cast,
            binding.kksPlayerServiceView,
            false
        ) as MediaRouteButton
        CaaS.setup(context, mediaRouteButton)
        return mediaRouteButton
    }
}
