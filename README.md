# BV One Android Player SDK

# Integration Guide for Developers
The tutorial will guide the developer the detailed flow to understand how to integrate with the Android Player SDK step by step in your application.
The Android Player SDK, called UniPlayer provides convenient API about DRM, media controller and a generic graphic user interface. If the generic UI doesn't fit your needs, you can easily customize your own UI through the provided API.


## Get started
### Gradle
```
android {
    compileSdk 31

    defaultConfig {
        ...
        minSdk 21
        targetSdk 31
        ...
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '11'
        freeCompilerArgs = ['-Xjvm-default=compatibility']
    }
}
```

### For using UniPlayer, need below library:
- paas & daas
- kksplayer
- kks-network

Library's dependency:
- com.google.guava:guava:28.0-android
- com.google.ads.interactivemedia.v3:interactivemedia:3.24.0
- com.google.android.gms:play-services-cast-framework:21.1.0
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.4
- okhttp
    - com.squareup.okhttp3:okhttp:3.12.2
    - com.squareup.okhttp3:logging-interceptor:3.12.2
- com.google.code.gson:gson:2.8.5
- Glide
    - com.github.bumptech.glide:glide:4.9.0
    - com.github.bumptech.glide:compiler:4.9.0 (kapt)

### Gradle.properties
```
android.enableDexingArtifactTransform.desugaring=false
android.enableJetifier=true
```


## Import method
### local AAR files
Add following `.aar` files to `libs/`.
```
// UniPlayer
kks-playcraft-daas.aar
kks-playcraft-paas.aar
```
```
// KKSPlayer
kksplayer.aar
kksplayer-kkdrm.aar
kksplayer-library-core-release.aar
kksplayer-library-common-release.aar
kksplayer-library-extractor-release.aar
kksplayer-library-dash-release.aar
kksplayer-library-ui-release.aar
```
```
// KKS-network
kks-network.aar
```

### Private maven bucket
- add below url in gradle repositories
```
// UniPlayer
maven {
    url "https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-trc/android/libs"
}
```
```
// KKSPlayer & KKSNetwork
maven {
    url "https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-ottfs/android/libs"
}
```
- Setting for gradle file
```
// UniPlayer
implementation "com.kkstream.android.ottfs.player:kks-playcraft-daas:" + paas_version
implementation "com.kkstream.android.ottfs.player:kks-playcraft-paas:" + paas_version

// KKSPlayer
implementation "com.kkstream.android.ottfs.player:kksplayer-kkdrm:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer-library-core:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer-library-dash:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer-library-ui:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer-library-common:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer-library-extractor:" + kksplayer_version
implementation "com.kkstream.android.ottfs.player:kksplayer:" + kksplayer_version

// KKSNetwork
implementation "com.kkstream.android.ottfs.network:kks-network:" + kknetwork_version
```
- others (can be imported from public maven)
```
plugins {
    ...
    id 'kotlin-kapt'
}

api ('com.google.guava:guava:' + guava_version) {
    // Exclude dependencies that are only used by Guava at compile time
    // (but declared as runtime deps) [internal b/168188131].
    exclude group: 'com.google.code.findbugs', module: 'jsr305'
    exclude group: 'org.checkerframework', module: 'checker-compat-qual'
    exclude group: 'com.google.errorprone', module: 'error_prone_annotations'
    exclude group: 'com.google.j2objc', module: 'j2objc-annotations'
    exclude group: 'org.codehaus.mojo', module: 'animal-sniffer-annotations'
}

//Google IMA SDK
implementation "com.google.ads.interactivemedia.v3:interactivemedia:$google_ima_version"

//Chromecast
implementation "com.google.android.gms:play-services-cast-framework:$cast_version"

//Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

//Okhttp
implementation 'com.squareup.okhttp3:okhttp:' + okhttp_version
implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

// gson
implementation 'com.google.code.gson:gson:' + gsonVersion

// thumbnail
implementation "com.github.bumptech.glide:glide:$glideVersion"
kapt "com.github.bumptech.glide:compiler:$glideVersion"
```

# Using UniPlayer
This section will show how to basically play media step by step

## Put the player's view (UniView) in xml
```xml=
...
<FrameLayout
    android:id="@+id/player_view_root"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <com.kkstream.playcraft.paas.player.mobile.UniView
        android:id="@+id/kks_player_service_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
...
```

## Setup the type of control panel
```kotlin=
binding.kksPlayerServiceView.setupControlPanel(
    autoKeepScreenOnEnabled = SampleApp.appSetting.autoKeepScreenOn,
    defaultContentType = ContentType.EMBEDDED
)
```
## Creating the player and attach it to player view
- UniPlayer need a license key generated by *BlendVision* CMS, or player will pop-up an error when you try to playback some contents.
```kotlin=
private var player: UniPlayer? = null

player = UniPlayer.Builder(
    requireContext(),
    PlayerConfig(
        license = "YOUR_LICENSE_KEY"
    )
).build()

binding.kksPlayerServiceView.setUnifiedPlayer(player)
```
- You can also set the license in the AndroidManifest.xml by using `UNI_PLAYER_LICENSE_KEY` keyword. The order UniPlayer adopts is first check `PlayerConfig` and then check `meta-data`.
```xml=
<meta-data
    android:name="UNI_PLAYER_LICENSE_KEY"
    android:value="YOUR_LICENSE_KEY" />
```

## [Optional] Configure player optional setting
```kotlin=
player?.setPlayerOptions(
    PlayerOptions(
        isThumbnailSeekingEnabled = true
    )
)
```

## Prepare media content
```kotlin=
val mediaConfig = MediaConfig(
    source = listOf(
        MediaConfig.Source(
            url = "DASH_URL",
            protocol = MediaConfig.Protocol.DASH,
            drm = MediaConfig.DrmInfo.Widevine(
                licenseServiceUrl = "LICENSE_SERVER_URL",
                header = mapOf("KEY" to "VALUE")
            )
        )
    ),
    title = "CONTENT_TITLE",
    imageUrl = "COVER_IMAGE",
    thumbnailSeekingUrl = "VTT_FOR_THUMBNAIL_SEEK_URL"
)
```
## Set default dialog for error handling
```kotlin=
val defaultDialogEventListener = DefaultDialogEventListener(requireActivity(), binding.kksPlayerServiceView)
binding.kksPlayerServiceView.setDialogEventListener(defaultDialogEventListener)
```
## Basic playback interface
```kotlin=
// play
player?.load(mediaConfig)
player?.start()
// pause
player?.pause()
// stop
player?.stop()
// release
player?.release()
```
- PS: UniView also provides a default player lifecycle, which can be called to easily map to the lifecycle of activity/fragment.
```kotlin=
binding.kksPlayerServiceView.onResume()
binding.kksPlayerServiceView.onStart()
binding.kksPlayerServiceView.onPause()
binding.kksPlayerServiceView.onStop()
binding.kksPlayerServiceView.onDestroy()
```

## Log/Error/player state listener
#### Log
```kotlin=
PlayerConfig(
    ...,
    playLogger = object : PlayLogger {
        override fun logEvent(eventName: String, properties: Map<String, Any>) {
            // do something
        }
    }
)
```
#### Error event
```kotlin=
player?.addErrorEventListener(object : ErrorEventCallback {
    override fun onUniError(errorEvent: UniErrorEvent): Boolean {
        // do something
        return true
    }
})
```
#### Player state
```kotlin=
player?.addStateEventListener(object : StateEventListener {
    override suspend fun onContentChanged(content: Content): UniErrorEvent? {
        // do something
        return null
    }

    override fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: PaaSProvider.PlaybackState
    ) {
        // PlaybackState {IDLE, BUFFERING, READY, ENDED}
    }

    override fun onVideoSizeChanged(
        width: Int,
        height: Int,
        unappliedRotationDegrees: Int,
        pixelWidthHeightRatio: Float
    ) {
        // do something
    }
})
```
## How to enable Picture in Picture mode
1. setup pinp handler within player view. (This feature is only supported on Android 8.0+)
```kotlin=
val handler = DefaultPictureInPictureHandler(requireActivity())
binding.uniView.setPictureInPictureHandler(
    handler
)
```
2. call entering method to enable the PinP, you can call it in lifecycle event, onClick event, guesture handle...etc.
```kotlin=
binding.kksPlayerServiceView.enterPictureInPicture()
```
3. override the `onPictureInPictureModeChanged` method
```kotlin=
override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode)

    if (isInPictureInPictureMode) {
        // 1. entering full screen
        // 2. hide the control UI
    } else {
        // 1. leaving full screen
    }

    // To reset the `isInPictureInPictureMode` parameter in `DefaultPictureInPictureHandler`, the following method needs to be called.
    binding.kksPlayerServiceView.onPictureInPictureModeChanged(isInPictureInPictureMode)
}
```
4. Declare PiP support
- By default, the system does not automatically support PiP for apps. If you want support PiP in your app, register your video activity in your manifest by setting android:supportsPictureInPicture to true. Also, specify that your activity handles layout configuration changes so that your activity doesn't relaunch when layout changes occur during PiP mode transitions.
- To ensure a single activity is used for video playback requests and switched into or out of PiP mode as needed, set the activity's android:launchMode to singleTask in your manifest.
```xml=
<activity
    android:name=".xxxx.xxxx"
    android:supportsPictureInPicture="true"
    android:launchMode="singleTask"
    android:configChanges="keyboardHidden|orientation|screenSize|smallestScreenSize|screenLayout" />
```
---

# CaaS (Option)

CaaS(Cast as a Service) provides `setup()` and `cast()` function to let you can integrate with Google Cast SDK conveniently.

## Integration

Following below steps to use Google Cast in your app. If you don't need to use Google Cast, you can skip this part.

1. Create a CastOptionProvider class
   ```kotlin
    /**
     * This class is needed for using Google Cast SDK, and may not be used in project code.
     */
    class CastOptionsProvider : CaaSCastOptionProvider() {
        override fun getReceiverAppId(context: Context): String {
            return "your_receiver_app_id"
        }

        // The target activity that you want to put the mini controller.
        override fun getTargetActivityClassName(): String? {
            return YourTargetActivity::class.java.name
        }
    }
   ```

   You can use ***CaaS*** default `CaaSExpandedController` activity.

   ```kotlin
    // The target activity that you want to put the mini controller.
    override fun getTargetActivityClassName(): String? {
        return CaaSExpandedController::class.java.name
    }
   ```
   Screenshot like below

   !<img width="261" height="524" alt="portfolio_view" src="./doc/minicontroller.jpg"> !<img width="261" height="524" alt="portfolio_view" src="./doc/minicontoller_click.jpg">

   If you don't like default activity ``CaaSExpandedController`,  just set a activity whatever you wanted but it must extend `ExpandedControllerActivity`

2. Add meta-data in your AndroidManifest
   ```xml
       <!--This meta data is used for Google Cast framework-->
       <meta-data
            android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
            android:value="package.path.to.your.CastOptionsProvider"/>
   ```

3. Add MediaRouteButton in your layout or menu.
   **In layout:**
      ```xml
      <androidx.mediarouter.app.MediaRouteButton
        android:id="@+id/castButton"
        android:layout_width="24dp"
        android:layout_height="24dp"
        app:actionProviderClass="androidx.mediarouter.app.MediaRouteActionProvider"
        app:showAsAction="always"
        app:mediaRouteButtonTint="@color/black"/>
      ```
   **In menu:**
      ```xml
    <item
        android:id="@+id/media_route_menu_item"
        android:title="@string/media_route_menu_title"
        app:actionProviderClass="androidx.mediarouter.app.MediaRouteActionProvider"
        app:showAsAction="always" />
      ```
   Then override the `onCreateOptionMenu()` for each of your activities.
      ```kotlin
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
          super.onCreateOptionsMenu(menu)
          menuInflater.inflate(R.menu.main, menu)
          CastButtonFactory.setUpMediaRouteButton(
              applicationContext, menu, R.id.media_route_menu_item
          )
          return true
    }
      ```

## CaaS Setup

1. Setup the cast button
    ```kotlin
    CaaS.setup(context, castButton)
    ```
2. Set `CastEventListener`
    ```kotlin
    CaaS.setListeners(object : CastEventListener {
        override fun onConnected() {}

        override fun onDisconnected() {}

        override fun onMessageReceive(castDevice: CastDevice, namespace: String, message: String) {}

        override fun onMessageReceiveError(errorMessage: String) {}

        override fun onRemoteClientPlayerStatusUpdated(playerState: Int) {}

        override fun onStartCasting(media: MediaQueueItem) {}

        override fun onStopCasting() {}

        override fun onMetaDataChanged(metadata: CastMetaData) {}
    })
    ```
3. Cast the video in `onConnected()`
    ```kotlin
    CaaS.cast(
        CastOptions(
            title = "${CONTENT_TITLE}",
            contentUrl = "${CONTENT_URL}",
            drmInfo = "${DRM_INFO_IN_MEDIA_CONFIG}",
            images = listOf(
                CastImage(
                    url = "${IMAGE_URL}"
                )
            )
        )
    )
    ```
4. Set the remote idle background image
    ```kotlin
    CaaS.setRemoteIdleImage("${IMAGE_URL}")
    ```

## Mini Controller

Add `CaaSMiniController` fragment in your layout.

```xml
<!--
    Add CaaSMiniController fragment in the layout of activity
    where you want to show the mini controller.
-->
<fragment
    android:id="@+id/castMiniController"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true"
    android:visibility="gone"
    class="com.kkstream.playcraft.caas.CaaSMiniController"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
```

## STB(Android TV)

### In layout file(.xml)

Add PaasTVFragment in your .xml file.
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    <fragment
            android:id="@+id/paasTVFragment"
            android:name="com.kkstream.playcraft.paas.player.common.service.tv.PaasTVFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### In activity

 **Setup paasTVFragment**
PaasTVFragment needs `Lifecycle` and [PaaSParameter](/doc/PaaSParameter.md) with following parameters:

```kotlin
paasTVFragment = supportFragmentManager.findFragmentById(R.id.paasTVFragment) as PaasTVFragment
...
paasTVFragment.setup(
                paasParam = kksPlayerServiceParamData,
                lifecycle = lifecycle)
```

## CustomPaaSTVFragmentInterface

[CustomPaaSTVFragmentInterface](/doc/CustomPaaSTVFragmentInterface.md) defines a interface to let project can control player state.

### Forward KeyEvent to PaasFragment
You need to override `onKeyUp` function and forward keyevent to PaasTVFragment.

`onKeyUp`
```kotlin
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (isFragmentHandleOnKeyUpEvent(keyCode, event)) {
            // If isFragmentHandleOnKeyUpEvent return true, it means we handle the onKeyUp event.
            // So we return true to tell parent we has handled it.
            true
        } else {
            super.onKeyUp(keyCode, event)
        }
    }

    private fun isFragmentHandleOnKeyUpEvent(keyCode: Int, event: KeyEvent?): Boolean {
        return (paasTVFragment as? FragmentKeyEventHandler)?.onKeyUp(keyCode, event) ?: false
    }
```

### Customize action button

There are default buttons below the seekbar and you can add customized buttons by the function, setupActionButtons,
of the PaasTVFragment. Customized buttons have to implement the ActionButton interface. Notice that
setupActionButtons should be called after the setup and the buttons are shown when the content type
is Videos. For example, the code below add a sample action to the module.

```kotlin
    paasTVFragment.setupActionButtons(createSampleActionButtons())

    private fun createSampleActionButtons(): List<ActionButton> {
            val sample = SampleActionButton(this, paasProvider, supportFragmentManager)
            return listOf(sample)
        }
```

!<img width="1025" height="578" alt="portfolio_view" src="./doc/tv_sample_action_button.png">

### Customize dialog fragment

If you want to show the customize dialog, the dialog should extend the PaaSTVDialogFragment to handle
some focus and MediaSession callback behavior.

### Customize theme color

There are three way to set the paas theme color

#### First - Add `paasThemeColor` in your theme

```xml
    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.Leanback" >
        <item name="paasThemeColor">@color/jcom_orange</item>
    </style>
```

#### Second - `setTheme` before `setContentView` in `onCreate` Stage
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.UpdateCustomizeIconColor)
        setContentView(R.layout.activity_playback)
        ...
    }
```
```xml
    <style name="UpdateCustomizeIconColor">
        <item name="paasThemeColor">@color/paas_tv_theme_color</item>
    </style>
```

#### Third -  Use `setPaasThemeColorStyle` to set theme color in programming
``` kotlin
    paasTVFragment.setPaasThemeColorStyle(R.style.UpdateCustomizeIconColor)
```
```xml
    <style name="UpdateCustomizeIconColor">
        <item name="paasThemeColor">@color/paas_tv_theme_color</item>
    </style>
```

Override `paas_tv_seek_bar_progress_background_color` in `color.xml`

```xml
<color name="paas_tv_theme_color">specific color</color>
```

<img width="1025" height="578" alt="portfolio_view" src="./doc/theme.png">

## Customize error message

Add same string ID in `string.xml` to override.

### Error Type
PaaS has five Error Type: `BasicNetworkError`, `GoogleCastErrorType`, `PlayerErrorType` and `PaaSInternalErrorType`

### How to find mapping String ID

#### Step 1: Find prefix of error type
BasicNetworkError -> `basic_network_error_#`

GoogleCastErrorType -> `google_cast_error_#`

PlayerErrorType -> `player_error_#`

PaaSInternalErrorType -> `paas_internal_error_#`

#### Step 2: Find error code

According to below code to get error code

#### Final:

Concatenate result of step1 and step2. ex.`google_cast_error_0`

```kotlin
enum class BasicNetworkErrorType(val code: Int) {
    // Basic Network Error
    NETWORK_ERROR(1001),
    // Signals that an error occurred while attempting to connect a
    // socket to a remote address and port.  Typically, the connection
    // was refused remotely (e.g., no process is listening on the
    // remote address/port).
    CONNECT_ERROR(1002),
    // Signals that a timeout has occurred on a socket read or accept.
    SOCKET_TIMEOUT_ERROR(1003),
    // Indicates that the client and server could not negotiate the
    // desired level of security.  The connection is no longer usable.
    SSL_HANDSHAKE_ERROR(1004),
    // Device's system time is smaller than end time of the certificate at the server.
    SSL_HANDSHAKE_CERT_NOT_YET_VALID_ERROR(1005),
    // Device's system time is larger than end time of the certificate at the server.
    SSL_HANDSHAKE_CERT_EXPIRED_ERROR(1006)
}

enum class PlayerErrorType(val code: Int) {
    // Player Error
    DECODER_INITIALIZATION(800),
    DECODER_QUERY(801),
    AUDIO_CONFIGURATION(802),
    AUDIO_INITIALIZATION(803),
    AUDIO_WRITE(804),
    AUDIO_VIDEO_DECODER(805),
    METADATA_DECODER(806),
    SUBTITLE_DECODER(807),
    MEDIA_SOURCE(808),
    DRM_SESSION(809),
    UNSUPPORTED_DRM(810),
    NO_DRM_INFO(811),
    CRYPTO(812),
    CRYPTO_NO_KEY(813),
    CRYPTO_KEY_EXPIRED(814),
    CRYPTO_RESOURCE_BUSY(815),
    CRYPTO_INSUFFICIENT_OUTPUT_PROTECTION(816),
    CRYPTO_SESSION_NOT_OPENED(817),
    CRYPTO_UNSUPPORTED_OPERATION(818),
    MULTI_MEDIA_CLOCKS(819)
}

enum class PaaSInternalErrorType(val code: Int) {
    CONTENT_EXPIRED(700),
    INVALID_URL(900),
    PROGRAM_END(901),
    NO_DOWNLOAD_FILE(902),
    NO_DOWNLOADER(903),
    NO_MANIFEST(904),
    CASTING_OFFLINE(905),
    NO_SELECTED_QUALITY_PROFILE(906)
}
```

## PaasTVFragment control state type

```kotlin
enum class StateType {
    NORMAL_PLAY_PAUSE,  // Focused on play and pause button
    NORMAL_OTHER, // Focused on restart, previous and next button
    END_ROLL,
    SEEK_MODE,
    RECOMMENDATION,
    WITHOUT_CONTROL_PANEL
}
```

## DEBUG

You can set the property `log.tag.PartyBoy` to `Debug` for get more detail log.

Example

```shell
adb shell setprop log.tag.PartyBoy Debug
```



# DaaS

Daas(Download as a service) provide download data and logic without UI. You can use Daas to implement download feature.

## Setup
### Initialize Downloader

```kotlin
val downloader =  Downloader(
            userId = userId,
            context = context,
            hostUrl = downloadServerUrl,
            deviceId = provideDeviceId(context),
            accessToken = accessToken,
            downloadWifiOnly = downloadWifiOnly,
            customHeaders = downloadCustomHeaders
            )
```

## Usage
Downloader provides following functions to fulfil the needs of downloading

```kotlin
    /**
     * Get downloadable list of the [itemId]
     */
    fun getDownloadableList(
        itemId: Int,
        onResponse: (Response<List<Downloadable>>) -> Unit
    )

    /**
     * Transform [liveDownloadList] to filter downloads whose id is in [ids] list
     */
    fun getDownloadsLiveData(ids: List<Int>): LiveData<List<Download>>

    /**
    * Get download in download list by id and return null if not found
    */
    fun getDownload(id: Int): Download?

    /**
     * Cancel the task to fetch the downloadable list of the [itemId]
     */
    fun cancelGetDownloadList(itemId: Int)

    /**
     * Start to download the video. You should provide the [download] then downloader
     * will know mpd url and which representation to download. The representation with
     * highest bandwidth will be selected to download if there are more than one representation
     * for video and audio.
     */
    fun download(download: Download)

    /**
     * Check if download is already inserted in Downloader.
     */
    fun isDownloadExist(id: Int): Boolean

    /**
     * Update the playback position. If download update playback position successfully, [liveDownloadList] will be
     * notified.
     */
    fun updatePlaybackPosition(download: Download, playbackPosition: Long): Boolean

    /**
     * Update the fist play timestamp if it is not played before. If download update fist play
     * timestamp successfully, [liveDownloadList] will be notified.
     */
    fun updateFirstPlayTime(download: Download): Boolean

    /**
     * Pause download. If download pause success, you will receive download state change callback or observe download onChanged.
     * The download's state will transfer to PAUSE.
     */
    fun pause(download: Download): Boolean

    /**
     * Resume download. If download resume success, you will receive download state change callback or observe download onChanged.
     * The download's state will transfer from PAUSE to DOWNLOADING.
     */
    fun resume(download: Download): Boolean

    /**
     * Renew expired downloads.
     * if download not completed, it will reschedule to the download queue
     */
    fun renew(download: Download)

    /**
     * Delete downloads in the downloader and sync with server
     */
    fun delete(downloads: List<Download>)

    /**
     * Sync downloads with server
     */
    fun sync()

    /**
     * Delete downloads which is not belonging to the current account id.
     */
    fun deleteOtherAccountsDownloadsInDB()
```

### Extensions
User following function to get Download data

```kotlin
    /**
     * This function will give all downloads belong to series with [id],
     */
    fun List<Download>.getDownloadsBySeriesId(id: String): List<Download>

    /**
     * This function will give all seasons belong to series with [id]
     */
    fun List<Download>.getSeasonsBySeriesId(id: String): List<Season>

    /**
     * This function will give all download belong to series with [seriesId] and season with [seasonId]
     */
    fun List<Download>.getDownloadsBySeriesIdAndSeasonId(
        seriesId: String,
        seasonId: String
    ): List<Download>

```
# ProGuard
To use PaaS and DaaS in released build. Please follow below proguard file, or can goto *sample/proguard-rules.pro* as reference
```kotlin
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.kkstream.playcraft.ottfs.playerservice.api.response.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------
```
