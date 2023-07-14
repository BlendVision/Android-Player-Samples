package com.kkstream.basicapi.network.data.response

import com.google.gson.annotations.SerializedName

data class PlaybackTokenResponse(
    @SerializedName("token")
    val playbackToken: String
)
