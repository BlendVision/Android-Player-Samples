package com.kkstream.basicapi.network.data.request

import com.google.gson.annotations.SerializedName

data class PlaybackTokenRequest(
    @SerializedName("resource_id")
    val resourceId: String,
    @SerializedName("resource_type")
    val resourceType: String,
    @SerializedName("customer_id")
    val customerId: String
)
