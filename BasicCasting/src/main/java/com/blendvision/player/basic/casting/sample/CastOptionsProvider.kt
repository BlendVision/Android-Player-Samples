package com.blendvision.player.basic.casting.sample

import android.content.Context
import com.blendvision.player.playback.cast.player.CaaSCastOptionProvider
import com.blendvision.player.playback.cast.player.CaaSExpandedController

class CastOptionsProvider : CaaSCastOptionProvider() {
    override fun getReceiverAppId(context: Context): String {
        return "YOUR_RECEIVER_APP_ID"
    }

    override fun getTargetActivityClassName(): String {
        return CaaSExpandedController::class.java.name
    }
}