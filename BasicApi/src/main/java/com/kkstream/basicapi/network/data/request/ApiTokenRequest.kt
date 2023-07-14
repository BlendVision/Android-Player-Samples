package com.kkstream.basicapi.network.data.request

import com.google.gson.annotations.SerializedName

data class ApiTokenRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("expired_date")
    val expiredDate: String
)
