package com.blendvision.player.basic.casting.sample

import android.content.Context
import com.kkstream.playcraft.caas.player.CaaSCastOptionProvider
import com.kkstream.playcraft.caas.player.CaaSExpandedController


class CastOptionsProvider : CaaSCastOptionProvider() {
    override fun getReceiverAppId(context: Context): String {
        return "YOUR_RECEIVER_APP_ID"
    }

    override fun getTargetActivityClassName(): String {
        return CaaSExpandedController::class.java.name
    }
}