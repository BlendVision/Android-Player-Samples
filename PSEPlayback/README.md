## Integration Guide

This guide shows not only how to integrate the BlendVision Player SDK into your application, but also how to enable PSE playback.

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

    <com.blendvision.player.playback.presentation.UniView
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
    licenseKey = "YOUR_LICENSE_KEY"
  )
).build()

binding.playerView.setUnifiedPlayer(player)
```

## Prepare media content and enable PSE
- To enable PSE, need the following criteria
    1. Content must not be DRM protected
    2. Player license allowed PSE feature
    3. `MediaConfig` should set the `Feature.PSE`

- Example
```kotlin
val mediaConfig = MediaConfig(
    source = MediaConfig.Source(
            url = "DASH_URL",
            protocol = MediaConfig.Protocol.DASH,
            drm = MediaConfig.DrmInfo.Widevine(
                licenseServiceUrl = "LICENSE_SERVER_URL",
                header = mapOf("KEY" to "VALUE")
            )
        ),
    title = "CONTENT_TITLE",
    imageUrl = "COVER_IMAGE",
    thumbnailSeekingUrl = "VTT_FOR_THUMBNAIL_SEEK_URL",
    // [Notice] Need to set below PSE configuration, or PSE won't work
    features = listOf(MediaConfig.Feature.PSE),
    /**
     * PSE mode
     * - Standard means the quality will upgrade to next level of 720p, and bitrate keep as same as 720p
     * - Saving means the quality will almost the same as 720p, but the bitrate downgrade to next level
     *
     * Default setting is STANDARD
     */
    pseMode = MediaConfig.PSEMode.STANDARD
)
```

**Noted**: When PSE is enabled, `setQuality(...)` will return `false` means invalid setting because of PSE has its own settings.

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
        override fun onLogEvent(logEvent: LogEvent, properties: Map<LogProperty, Any>) {
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
        playerState: PaaSProvider.PlayerState
    ) {
        // PlayerState {IDLE, BUFFERING, READY, ENDED}
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

