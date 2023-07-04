## Develop Environment Requirements
- Kotlin version 1.6+

## Dependencies
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

# Integration Guide for Developers
The tutorial will guide the developer the detailed flow to understand how to integrate with the Android Player SDK step by step in your application. The Android Player SDK, called UniPlayer provides convenient API about DRM, media controller and a generic graphic user interface. If the generic UI doesn't fit your needs, you can easily customize your own UI through the provided API.

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

   !<img width="261" height="524" alt="portfolio_view" src="https://github.com/BlendVision/Android-Player-SDK/assets/129143433/028594ee-c959-4875-a5dd-c761912c1d3f"> !<img width="261" height="524" alt="portfolio_view" src="https://github.com/BlendVision/Android-Player-SDK/assets/129143433/fb86d181-e3ca-4bdc-acb0-5ef9f805f136">

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
