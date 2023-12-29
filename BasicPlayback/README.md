## Integration Guide

This guide details how to integrate the BlendVision Player SDK into your application. Our player provides
convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

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

