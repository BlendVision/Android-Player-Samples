package com.blendvision.player.basic.casting.sample

import android.content.Context
import com.blendvision.player.playback.cast.presentation.CaaSCastOptionProvider
import com.blendvision.player.playback.cast.presentation.CaaSExpandedController

class CastOptionsProvider : CaaSCastOptionProvider() {
    override fun getReceiverAppId(context: Context): String {
        return "YOUR_RECEIVER_APP_ID"
    }

    override fun getTargetActivityClassName(): String {
        return CaaSExpandedController::class.java.name
    }
}