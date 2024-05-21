## Integration Guide

This guide details how to integrate the BlendVision Player SDK into your application. Our player provides convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

# Prerequisites

To implement audio capabilities in your app, please follow the steps outlined in the [**BasicPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicPlayback) section.

## Basic Usage

### Step1: Set player layout
```xml
<com.blendvision.player.playback.presentation.UniView
    android:id="@+id/playerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```
### Step2: Initialize player

```kotlin
private fun initializePlayer() {
    val playerConfig = PlayerConfig(isLoopEnabled = true, playLogger = object : PlayLogger {
        override fun logEvent(eventName: String, properties: Map<String, Any>) {
            // log event callback
        }
    })
    val player = UniPlayer.Builder(context = context, playerConfig = playerConfig).build()
    
    binding.playerView.setupControlPanel(autoKeepScreenOnEnabled = true, defaultPanelType = PanelType.EMBEDDED, disableControlPanel = null)
    binding.playerView.setUnifiedPlayer(player)
}
```

### Step3: Custom background image
When playing an audio stream, by default, the background is black, you customize the background image with setPoster(url) method, like the following snippet:

```kotlin
//set poster between player and control panel
player.setPoster("YOUR_IMAGE_URL")

//If you want to remove the poster, you can do that by using
player.setPoster(null)
```

### Step4: Load and control media
```kotlin
// Media Configuration
val mediaConfig = MediaConfig(
     source = listOf(
            MediaConfig.Source(
                url = "MPD_URL",
                protocol = MediaConfig.Protocol.DASH
            )
        ),
        title = "TITLE",
        imageUrl = "COVER_IMAGE",
        thumbnailSeekingUrl = null,
        playWhenReady = true,
        sharedUrl = "SHARED_INFO",
        description = "DESC_INFO"
)

// Load and Control Media
player.load(mediaConfig)
player.start()
player.pause()
player.play()
player.stop()
player.release()
```
