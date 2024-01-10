# BlendVision Player Android Samples

This repository contains sample apps using the BlendVision Player Android SDK.  
This guide details how to integrate the BlendVision Player SDK into your application.  
Our player provides convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

## Requirements

- **IDE**: Android Studio 3.0 or later
- **minSdkVersion**: 21
- **targetSdkVersion**: 33
- **Kotlin Version**: 1.7.x or later

## Importing AAR into Project

There are multiple ways to integrate an AAR file into your Android project. Below are the methods,
including a simplified approach using `implementation fileTree`.  
To ensure you have the most recent features and updates, download the latest version of the AAR files from the BlendVision Android Player SDK repository.  
Access the latest releases at: [BlendVision Android Player SDK Releases](https://github.com/BlendVision/Android-Player-SDK/releases).  
Upon visiting the link, you will find the following AAR files available for download:

#### BlendVision Player Core SDK
- `kks-playback.aar`
- `kks-download.aar`
- `kks-analytics.aar`
- `kks-common.aar`

#### KKSPlayer
- `kksplayer.aar`
- `kksplayer-library-core-release.aar`
- `kksplayer-library-common-release.aar`
- `kksplayer-library-extractor-release.aar`
- `kksplayer-library-dash-release.aar`
- `kksplayer-library-ui-release.aar`

#### KKS-Network
- `kks-network.aar`

### Manual Import with `fileTree`

1. **Place AAR File in `libs` Directory**: Ensure the `.aar` (and/or `.jar`) file is located within
   the `libs` directory of your Android project.

2. **Update `build.gradle`**: In your app-level `build.gradle` file, add the following line in
   the `dependencies` block:

    ```groovy
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
    ```

   This will include all `.jar` and `.aar` files that are in the `libs` directory into your project.


## Add the dependencies to your app-level Gradle file
```groovy

    //coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

    //Chromecast
    implementation "com.google.android.gms:play-services-cast-framework:21.1.0"

    //Glide
    implementation "com.github.bumptech.glide:glide:4.15.1"

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

    implementation ("com.google.guava:guava:27.1-android") {
       // Exclude dependencies that are only used by Guava at compile time
       // (but declared as runtime deps) [internal b/168188131].
       exclude group: 'com.google.code.findbugs', module: 'jsr305'
       exclude group: 'org.checkerframework', module: 'checker-compat-qual'
       exclude group: 'com.google.errorprone', module: 'error_prone_annotations'
       exclude group: 'com.google.j2objc', module: 'j2objc-annotations'
       exclude group: 'org.codehaus.mojo', module: 'animal-sniffer-annotations'
    }

```

> **Note**: After making changes, don't forget to sync your Gradle files to ensure that the project
> compiles successfully.

## Sample App Setup Instructions

### Player license
BlendVision Player license key is obtained by logging into BlendVision CMS (https://app.one.blendvision.com/en/dashboard) and navigating to `VOD -> select one of content -> share icon -> Player SDK -> License Key` to find the license.

#### The license key can be add by following two ways:
1. Dynamically coding (license lifecycle scope only for current player instance)
    ```kotlin
    private var player: UniPlayer? = null
    
    player = UniPlayer.Builder(
        requireContext(),
        PlayerConfig(
            license = "{YOUR_PLAYER_LICENSE}"
        )
    ).build()
    
    binding.kksPlayerServiceView.setUnifiedPlayer(player)
    ```
2. Application manifest (license lifecycle scope for all players in your application)
    ```xml
    <meta-data 
        android:name="UNI_PLAYER_LICENSE_KEY" 
        android:value="{YOUR_PLAYER_LICENSE}"
    />
    ```

> **Note**:
>   1. The order player adopts is first check PlayerConfig and then check meta-data.
>   2. If the license key is not correctly set, you will encounter a 20403 error.

## Available Sample Apps

All examples are provided in Kotlin :+1:

### Basics

[**BasicAnalysis**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicAnalysis):
Demonstrates the configuration for sending analytics events for basic analysis.  
[**BasicCasting**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicCasting):
Explains how to stream video and audio directly to a TV.  
[**BasicPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicPlayback):
Illustrates the setup for basic playback of DASH streams using UniPlayer.  
[**BasicTVPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicTVPlayback):
Illustrates the setup for basic TV playback of DASH streams using UniPlayer.  
[**BasicAudio**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicAudio):
Illustrates the setup for basic audio of DASH streams using UniPlayer.  
[**BasicDownload**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicDownload):
Illustrates the setup for basic download and offline playback using UniPlayer.

### Advanced

[**AdvancedPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/AdvancedPlayback): Illustrates
the setup for playback of DASH streams with DRM support, enable Thumbnail Seeking, UI Customization
using UniPlayer.


