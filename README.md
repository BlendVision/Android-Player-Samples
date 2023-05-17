# Integration Guide for Developers
The tutorial will guide the developer the detailed flow to understand how to integrate with the Android Player SDK step by step in your application.
The Android Player SDK, called UniPlayer provides convenient API about DRM, media controller and a generic graphic user interface. If the generic UI doesn't fit your needs, you can easily customize your own UI through the provided API.

## Get started
Gradle
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
## For using UniPlayer, need below library:

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

Gradle.properties
```
android.enableDexingArtifactTransform.desugaring=false
android.enableJetifier=true
```

## Import method
### local AAR files
- Add following .aar files to libs/.
```
// UniPlayer
kks-playcraft-daas.aar
kks-playcraft-paas.aar
// KKSPlayer
kksplayer.aar
kksplayer-kkdrm.aar
kksplayer-library-core-release.aar
kksplayer-library-common-release.aar
kksplayer-library-extractor-release.aar
kksplayer-library-dash-release.aar
kksplayer-library-ui-release.aar
// KKS-network
kks-network.aar
```

### Private maven bucket
- Add below url in gradle repositories
```
// UniPlayer
maven {
   url "<https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-trc/android/libs">
}
// KKSPlayer & KKSNetwork
maven {
   url "<https://kks-devops-assets.s3-ap-northeast-1.amazonaws.com/kks-ottfs/android/libs">
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

- Others (can be imported from public maven)
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

//Google IMA SDK, used in PaaS
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
```
...
<FrameLayout
   android:id="@+id/player_view_root"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   app:layout_constraintEnd_toEndOf="parent"
   app:layout_constraintStart_toStartOf="parent"
   app:layout_constraintTop_toTopOf="parent">

   <com.kkstream.playcraft.paas.player.mobile.UniSView
   android:id="@+id/kks_player_service_view"
   android:layout_width="match_parent"
   android:layout_height="match_parent" />

</FrameLayout>
...
```

## Setup the type of control panel
```
binding.kksPlayerServiceView.setupControlPanel(
   autoKeepScreenOnEnabled = SampleApp.appSetting.autoKeepScreenOn,
   defaultContentType = ContentType.EMBEDDED
)
```

## Creating the player and attach it to player view
UniPlayer need a license key generated by BlendVision CMS, or player will pop-up an error when you try to playback some contents.
```
private var player: UniPlayer? = null

player = UniPlayer.Builder(
   requireContext(),
   PlayerConfig(
       license = "YOUR_LICENSE_KEY"
   )
).build()

binding.kksPlayerServiceView.setUnifiedPlayer(player)
 
You can also set the license in the AndroidManifest.xml by using UNI_PLAYER_LICENSE_KEY keyword. The order UniPlayer adopts is first check PlayerConfig and then check meta-data.
<meta-data
   android:name="UNI_PLAYER_LICENSE_KEY"
   android:value="YOUR_LICENSE_KEY" />
```

## Configure player optional setting (Optional)
```
player?.setPlayerOptions(
   PlayerOptions(
       isThumbnailSeekingEnabled = true
   )
)
```

## Prepare media content
```
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
```
val defaultDialogEventListener = DefaultDialogEventListener(requireActivity(), binding.kksPlayerServiceView)
binding.kksPlayerServiceView.setDialogEventListener(defaultDialogEventListener)
```

##Basic playback interface
```
// play
player?.load(mediaConfig)
player?.start()
// pause
player?.pause()
// stop
player?.stop()
// release
player?.release()

binding.kksPlayerServiceView.onResume()
binding.kksPlayerServiceView.onStart()
binding.kksPlayerServiceView.onPause()
binding.kksPlayerServiceView.onStop()
binding.kksPlayerServiceView.onDestroy()
```
PS: UniView also provides a default player lifecycle, which can be called to easily map to the lifecycle of activity/fragment.

## Log/Error/player state listener
### Log
```
PlayerConfig(
   ...,
   playLogger = object : PlayLogger {
       override fun logEvent(eventName: String, properties: Map<String, Any>) {
           // do something
       }
   }
)
```
### Error event
```
player?.addErrorEventListener(object : ErrorEventCallback {
   override fun onUniError(errorEvent: UniErrorEvent): Boolean {
       // do something
       return true
   }
})
```
### Player state
```
player?.addStateEventListener(object : StateEventListener {
   override suspend fun onContentChanged(content: Content): UniErrorEvent? {
       // do something
       return null
   }

   override fun onPlayerStateChanged(
       playWhenReady: Boolean,
       playbackState: UniProvider.PlaybackState
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
