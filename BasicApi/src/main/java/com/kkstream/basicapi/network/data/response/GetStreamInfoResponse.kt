package com.kkstream.basicapi.network.data.response

import com.google.gson.annotations.SerializedName

data class GetStreamInfoResponse(
    @SerializedName("sources")
    val sources: List<Source>
)

data class Source(
    @SerializedName("manifests")
    val manifests: List<Manifest>,
    @SerializedName("thumbnail_seeking_url")
    val thumbnailSeekingUrl: String
)

data class Manifest(
    @SerializedName("protocol")
    val protocol: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("resolutions")
    val resolutions: List<Resolution>
)

data class Resolution(
    @SerializedName("height")
    val height: String,
    @SerializedName("width")
    val width: String
)