package com.kkstream.basicapi.network.data.response

import com.google.gson.annotations.SerializedName

data class StartSessionResponse(
    @SerializedName("drm_server_endpoint")
    val endPoint: String
)
