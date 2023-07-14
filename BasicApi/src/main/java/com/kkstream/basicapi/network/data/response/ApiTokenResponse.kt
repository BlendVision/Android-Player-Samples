package com.kkstream.basicapi.network.data.response

import com.google.gson.annotations.SerializedName

data class ApiTokenResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("expired_date")
    val expiredDate: String,
    @SerializedName("token_type")
    val tokenType: String,
)
