## Integration Guide

This guide details how to integrate the BlendVision Player SDK into your application. Our player provides
convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

## Dependencies

- com.google.guava:guava
- com.google.android.gms:play-services-cast-framework
- org.jetbrains.kotlinx:kotlinx-coroutines-android
- retrofit2
  - com.squareup.retrofit2:retrofit
  - com.squareup.retrofit2:converter-gson
- okhttp
  - com.squareup.okhttp3:okhttp
  - com.squareup.okhttp3:logging-interceptor
- com.google.code.gson:gson
- com.github.bumptech.glide:glide
- io.insert-koin:koin-android
- org.jetbrains.kotlinx:kotlinx-serialization-json
- androidx.datastore:datastore

## Get started

### Gradle

```groovy
android {
    compileSdk 33

    defaultConfig {
        ...
        minSdk 21
        targetSdk 33
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

```groovy
// BlendVision Player Core SDK
kks-playback.aar
kks-download.aar
kks-analytics.aar
kks-common.aar
```

```groovy
// KKSPlayer
kksplayer.aar
kksplayer-library-core-release.aar
kksplayer-library-common-release.aar
kksplayer-library-extractor-release.aar
kksplayer-library-dash-release.aar
kksplayer-library-ui-release.aar
```

```groovy
// KKS-network
kks-network.aar
```

### Private maven bucket

- add below url in gradle repositories

```groovy
// UniPlayer
maven {
    url "https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-cpt/android/libs"
}
```

```groovy
// KKSPlayer & KKSNetwork
maven {
    url "https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-ottfs/android/libs"
}
```

- Setting for gradle file

```groovy

// KKSPlayer
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

```groovy

api ('com.google.guava:guava:' + guava_version) {
    // Exclude dependencies that are only used by Guava at compile time
    // (but declared as runtime deps) [internal b/168188131].
    exclude group: 'com.google.code.findbugs', module: 'jsr305'
    exclude group: 'org.checkerframework', module: 'checker-compat-qual'
    exclude group: 'com.google.errorprone', module: 'error_prone_annotations'
    exclude group: 'com.google.j2objc', module: 'j2objc-annotations'
    exclude group: 'org.codehaus.mojo', module: 'animal-sniffer-annotations'
}


//Chromecast
implementation "com.google.android.gms:play-services-cast-framework:$cast_version"

//Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

//Okhttp
implementation 'com.squareup.okhttp3:okhttp:' + okhttp_version
implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

//Retrofit2
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"

//gson
implementation 'com.google.code.gson:gson:' + gsonVersion

//glide
implementation "com.github.bumptech.glide:glide:$glideVersion"

//koin
implementation "io.insert-koin:koin-android:$koinVersion"

//kotlinx serialization
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"

//proto datastore
implementation "androidx.datastore:datastore:$datastoreVersion"
```

# Using UniPlayer

This section will show how to basically play media step by step

## Put the player's view (UniView) in xml

```xml

<FrameLayout
    android:id="@+id/player_view_root"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <com.blendvision.player.playback.player.mobile.UniView
        android:id="@+id/playerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>

```

## Setup the type of control panel

```kotlin
binding.playerView.setupControlPanel(
    autoKeepScreenOnEnabled = true,
    defaultPanelType = PanelType.EMBEDDED
)
```

## Creating the player and attach it to player view

```kotlin
private var player: UniPlayer? = null

player = UniPlayer.Builder(
  requireContext(),
  PlayerConfig(
    license = "YOUR_LICENSE_KEY"
  )
).build()

binding.playerView.setUnifiedPlayer(player)
```

## [Optional] Configure player optional setting

```kotlin
player?.setPlayerOptions(
    PlayerOptions(
        isThumbnailSeekingEnabled = true
    )
)
```

## Prepare media content

```kotlin
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

```kotlin
val defaultDialogEventListener = DefaultDialogEventListener(requireActivity(), binding.playerView)
binding.playerView.setDialogEventListener(defaultDialogEventListener)
```

## Basic playback interface

```kotlin
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

- PS: UniView also provides a default player lifecycle, which can be called to easily map to the
  lifecycle of activity/fragment.

```kotlin
binding.playerView.onResume()
binding.playerView.onStart()
binding.playerView.onPause()
binding.playerView.onStop()
binding.playerView.onDestroy()
```

## Log/Error/player state listener

#### Log

```kotlin
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

```kotlin
player?.addErrorEventListener(object : ErrorEventCallback {
    override fun onUniError(errorEvent: UniErrorEvent): Boolean {
        // do something
        return true
    }
})
```

#### Player state

```kotlin
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

```kotlin
val handler = DefaultPictureInPictureHandler(requireActivity())
binding.playerView.setPictureInPictureHandler(
    handler
)
```

2. call entering method to enable the PinP, you can call it in lifecycle event, onClick event,
   guesture handle...etc.

```kotlin
binding.playerView.enterPictureInPicture()
```

3. override the `onPictureInPictureModeChanged` method

```kotlin
override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode)

    if (isInPictureInPictureMode) {
        // 1. entering full screen
        // 2. hide the control UI
    } else {
        // 1. leaving full screen
    }

    // To reset the `isInPictureInPictureMode` parameter in `DefaultPictureInPictureHandler`, the following method needs to be called.
    binding.playerView.onPictureInPictureModeChanged(isInPictureInPictureMode)
}
```

4. Declare PiP support

- By default, the system does not automatically support PiP for apps. If you want support PiP in
  your app, register your video activity in your manifest by setting android:
  supportsPictureInPicture to true. Also, specify that your activity handles layout configuration
  changes so that your activity doesn't relaunch when layout changes occur during PiP mode
  transitions.
- To ensure a single activity is used for video playback requests and switched into or out of PiP
  mode as needed, set the activity's android:launchMode to singleTask in your manifest.

```xml
<activity
    android:name=".xxxx.xxxx"
    android:supportsPictureInPicture="true"
    android:launchMode="singleTask"
    android:configChanges="keyboardHidden|orientation|screenSize|smallestScreenSize|screenLayout" />
```

---

