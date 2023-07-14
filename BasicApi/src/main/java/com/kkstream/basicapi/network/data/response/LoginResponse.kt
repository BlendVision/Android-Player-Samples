package com.kkstream.basicapi.network.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_in")
    val expiresIn: String,
    @SerializedName("token_type")
    val tokenType: String
)
