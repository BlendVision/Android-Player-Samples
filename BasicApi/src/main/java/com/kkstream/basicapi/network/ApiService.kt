package com.kkstream.basicapi.network

import com.kkstream.basicapi.network.data.request.ApiTokenRequest
import com.kkstream.basicapi.network.data.request.LoginRequest
import com.kkstream.basicapi.network.data.request.PlaybackTokenRequest
import com.kkstream.basicapi.network.data.response.ApiTokenResponse
import com.kkstream.basicapi.network.data.response.GetStreamInfoResponse
import com.kkstream.basicapi.network.data.response.LoginResponse
import com.kkstream.basicapi.network.data.response.PlaybackTokenResponse
import com.kkstream.basicapi.network.data.response.StartSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("bv/account/v1/accounts/login")
    suspend fun login(
        @Body req: LoginRequest
    ): LoginResponse

    @POST("/bv/account/v1/accounts/api-token")
    suspend fun getApiToken(
        @Header("Authorization") loginToken: String,
        @Body req: ApiTokenRequest
    ): ApiTokenResponse

    @POST("bv/cms/v1/tokens")
    suspend fun getPlaybackToken(
        @Header("x-bv-org-id") orgId: String,
        @Header("Authorization") apiToken: String,
        @Body req: PlaybackTokenRequest
    ): PlaybackTokenResponse

    @POST("bv/playback/v1/sessions/{deviceId}:start")
    suspend fun startPlaybackSession(
        @Header("Authorization") playbackToken: String,
        @Path("deviceId") deviceId: String
    ): StartSessionResponse

    @GET("bv/playback/v1/sessions/{deviceId}")
    suspend fun getStreamInfo(
        @Header("Authorization") playbackToken: String,
        @Path("deviceId") deviceId: String
    ): GetStreamInfoResponse

    // post this API every 10 seconds
    @POST("bv/playback/v1/sessions/{deviceId}:heartbeat")
    suspend fun sendHeartbeat(
        @Header("Authorization") playbackToken: String,
        @Path("deviceId") deviceId: String
    )

    @POST("bv/playback/v1/sessions/{deviceId}:end")
    suspend fun endPlaybackSession(
        @Header("Authorization") playbackToken: String,
        @Path("deviceId") deviceId: String
    )
}