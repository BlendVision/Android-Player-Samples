# Integration Guide

This guide details how to integrate the download and offline playback features of BlendVision Player SDK into your application. Our player provides
convenient APIs for DRM, media controllers, and a generic UI that can be customized as needed.

## Prerequisites
### Request POST_NOTIFICATIONS permission
We have applied for the permission internally in the manifest, but you still need to request it once at runtime.
```kotlin
requestPermissions(
   arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATION_PERMISSION_REQUEST_CODE 
)
```

## Downloader
### Initialize Downloader
Create a simple downloader instance without settings the track selector callback. Downloader will automatically download the content tracks by default.
```kotlin
val downloader =  Downloader.Builder(context).build()
```

### Track selection
You can set the track selector callback within the downloader builder.
```kotlin
   private val callback = object : DownloadTrackSelection.Callback {
      override suspend fun selectTrack(downloadableTracks: DownloadableTracks): DownloadTrackSelection {
         // do something
         return DownloadTrackSelection(...)
      }
   }
   
   val downloader = Downloader.Builder(context)
         .setTrackSelectorCallback(callback)
         .build()
```
#### DownloadableTracks & DownloadTrackSelection
- Our library will call back to your application with the `DownloadableTracks` parameter,
  which contains all the downloadable tracks for the content you just called the download method.
- You can use the `DownloadableTracks` to create your own business logic and then return the track id defined in the `DownloadableTracks` by `DownloadTrackSelection`, then downloader will download these tracks according to it.
- You can ignore the third parameter in `DownloadTrackSelection` which means that you want to download all subtitles by default.

> Note: If you return an empty `DownloadTrackSelection`, for example `return DownloadTrackSelection()`, this download process will be abandoned.

#### Limitations
- Each download task can only include one audio track and one video track, but can include multiple subtitle tracks.

### Usage
Downloader provides following functions to fulfil the needs of downloading

```kotlin
    /**
    * Flow for error Events of download processing
    *
    * @return {UniDownloadErrorEvent}
    */
   val errorEvent: Flow<UniDownloadErrorEvent>
   
   /**
    * Flow for download status, including {DownloadState} and progress in percentage
    */
   val downloadStatus: StateFlow<List<DownloadStatus>>
   
   /**
    * Start downloading for specified stream
    *
    * @param config the configuration for download content
    */
   fun download(config: DownloadConfig)
   
   /**
    * Pause downloading for specified stream
    */
   fun pause(config: DownloadConfig)
   
   /**
    * Resume downloading for specified stream
    */
   fun resume(config: DownloadConfig)
   
   /**
    * Remove an item while downloaded or still in downloading
    */
   fun remove(config: DownloadConfig)
   
   /**
    * Remove all items
    */
   fun removeAll()
   
   /**
    * Release the downloader.
    * We recommend that you should call this method when you don't need the downloader anymore.
    */
   fun release()
```

### Download Status
Used to observe the download status in the list.
```kotlin
data class DownloadStatus(
    val mpdUrl: String,
    val state: DownloadState,
    val percentDownloaded: Float
)
```

### Download State
- `QUEUE`
  - In download schedule
- `STOPPED`
  - pause the download process
- `DOWNLOADING`
  - downloading
- `COMPLETED`
  - download completed
- `FAILED`
  - download failed

## Offline playback
To playback the downloaded content in offline mode (no need network)
### Playback the downloaded media
Using `OfflineMediaConfig` which wrap the manifest you just downloaded to tell our player for offline mode playback.
```kotlin
   // prepare the config
   val offlineMediaConfig = OfflineMediaConfig(
       title = "Offline Testing"
       mpdUrl = "*.mpd"
   )
   
   // load and start the playback process
   player.load(offlineMediaConfig)
   player.start()
```

