# BlendVision Player Android Samples

This repository contains sample apps using the BlendVision Player Android SDK.  
This guide details how to integrate the BlendVision Player SDK into your application.  
Our player provides convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

## How to install BlendVision Player SDK:
[**Android-Player-SDK**](https://github.com/BlendVision/Android-Player-SDK)

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

[**BasicAnalysis**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicAnalysis):
Demonstrates the configuration for sending analytics events for basic analysis.  
[**BasicCasting**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicCasting):
Explains how to stream video and audio directly to a TV.  
[**BasicPlayback**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicPlayback):
Illustrates the setup for basic playback of DASH streams using UniPlayer.  
[**BasicTVPlayback**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicTVPlayback):
Illustrates the setup for basic TV playback of DASH streams using UniPlayer.  
[**BasicAudio**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicAudio):
Illustrates the setup for basic audio of DASH streams using UniPlayer.  
[**BasicDownload**](https://github.com/BlendVision/Android-Player-SDK/blob/main/BasicDownload):
Illustrates the setup for basic download and offline playback using UniPlayer.  
[**ULLPlayback**](https://github.com/BlendVision/Android-Player-SDK/blob/main/ULLPlayback):
Illustrates the setup for ultra-low latency playback of DASH streams using UniPlayer.  

### Advanced

[**AdvancedPlayback**](https://github.com/BlendVision/Android-Player-SDK/blob/main/AdvancedPlayback): Illustrates
the setup for playback of DASH streams with DRM support, enable Thumbnail Seeking, UI Customization
using UniPlayer.


