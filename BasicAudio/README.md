## Integration Guide

This guide details how to integrate the BlendVision Player SDK into your application. Our player provides convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

# Prerequisites

To implement audio capabilities in your app, please follow the steps outlined in the [**BasicPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicPlayback) section.  

## Importing AAR into Project

There are multiple ways to integrate an AAR file into your Android project. Below are the methods,
including a simplified approach using `implementation fileTree`.

### Manual Import with `fileTree`

1. **Place AAR File in `libs` Directory**: Ensure the `.aar` (and/or `.jar`) file is located within
   the `libs` directory of your Android project.

2. **Update `build.gradle`**: In your app-level `build.gradle` file, add the following line in
   the `dependencies` block:

    ```gradle
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
    ```

   This will include all `.jar` and `.aar` files that are in the `libs` directory into your project.

3. **Add Dependencies**

```gradle

    //coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

    //gson
    implementation "com.google.code.gson:gson:2.10.1"

    //retrofit2
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"

    //okhttp_logging_interceptor
    implementation "com.squareup.okhttp3:logging-interceptor:4.9.0"

    //koin
    implementation "io.insert-koin:koin-android:3.4.3"

    //kotlinx serialization
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1"

    //proto datastore
    implementation "androidx.datastore:datastore:1.0.0"

```

> **Note**: After making changes, don't forget to sync your Gradle files to ensure that the project
> compiles successfully.

## Basic Usage

### Step1: Set player layout
```xml
<com.blendvision.player.playback.player.mobile.UniView
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
    
    binding.playerView.setupControlPanel(autoKeepScreenOnEnabled = true, defaultPanelType = PanelType.EMBEDDED)
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
