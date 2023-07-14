package com.kkstream.basicapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kkstream.basicapi.databinding.ActivityMainBinding
import com.kkstream.basicapi.network.NetworkManager
import com.kkstream.basicapi.network.data.request.ApiTokenRequest
import com.kkstream.basicapi.network.data.request.LoginRequest
import com.kkstream.basicapi.network.data.request.PlaybackTokenRequest
import com.kkstream.basicapi.network.utils.SystemUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiService = NetworkManager().apiService

    private var apiToken: String? = null
    private var playbackToken: String? = null
    private var drmServerUrl: String? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "error: $throwable")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // Login & get a ApiToken
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                val loginRequest = LoginRequest(
                    email = EMAIL,
                    password = PASSWORD
                )
                val loginResponse = apiService.login(loginRequest)
                Log.i(TAG, "res: $loginResponse")

                val apiTokenRequest = ApiTokenRequest(
                    name = DESIRED_NAME_FOR_API_TOKEN,
                    expiredDate = EXPIRED_DATE // in ISO8601 format (e.g., "2023-12-31T23:59:59Z")
                )
                val apiTokenResponse = apiService.getApiToken(
                    loginToken = loginResponse.tokenType + " " + loginResponse.accessToken,
                    req = apiTokenRequest
                )
                Log.i(TAG, "res: $apiTokenResponse")
                apiToken = apiTokenResponse.tokenType + " " + apiTokenResponse.token
            }
        }

        // Get the playback token
        binding.btnGetPlaybackToken.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                val request = PlaybackTokenRequest(
                    resourceId = RESOURCE_ID,
                    resourceType = RESOURCE_TYPE,
                    customerId = ""
                )
                apiToken?.let { token ->
                    val response = apiService.getPlaybackToken(
                        orgId = ORG_ID,
                        apiToken = token,
                        req = request
                    )
                    Log.i(TAG, "res: $response")
                    playbackToken = response.playbackToken
                } ?: showToast("Empty Api Token")
            }
        }

        // Start a playback session
        binding.btnStartPlaybackSession.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                playbackToken?.let { token ->
                    val response = apiService.startPlaybackSession(
                        playbackToken = token,
                        deviceId = SystemUtil.provideDeviceId(this@MainActivity)
                    )
                    Log.i(TAG, "res: $response")
                    drmServerUrl = response.endPoint
                } ?: showToast("Empty Playback Token")
            }
        }

        // Get the stream information
        binding.btnGetStreamInfo.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                playbackToken?.let { token ->
                    Log.d(TAG, "token: $token")
                    val response = apiService.getStreamInfo(
                        playbackToken = token,
                        deviceId = SystemUtil.provideDeviceId(this@MainActivity)
                    )
                    Log.i(TAG, "res: $response")
                } ?: showToast("Empty Playback Token")
            }
        }

        // heartbeat
        binding.btnSendHeartbeat.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                playbackToken?.let { token ->
                    apiService.sendHeartbeat(
                        playbackToken = token,
                        deviceId = SystemUtil.provideDeviceId(this@MainActivity)
                    )
                } ?: showToast("Empty Playback Token")
            }
        }

        // End the playback session
        binding.btnEndSession.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                playbackToken?.let { token ->
                    apiService.endPlaybackSession(
                        playbackToken = token,
                        deviceId = SystemUtil.provideDeviceId(this@MainActivity)
                    )
                } ?: showToast("Empty Playback Token")
            }
        }
    }

    private suspend fun showToast(msg: String) = withContext(Dispatchers.Main) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val EMAIL = "xxxxxxxxxxx@kkcompany.com"
        private const val PASSWORD = "xxxxxxxx"
        private const val DESIRED_NAME_FOR_API_TOKEN = "test_name"
        private const val EXPIRED_DATE = "xxxx-xx-xxTxx:xx:xxx"
        private const val RESOURCE_ID = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        private const val RESOURCE_TYPE = "RESOURCE_TYPE_VOD"
        private const val ORG_ID = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
    }
}