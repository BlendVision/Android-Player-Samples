## Integration Guide

This guide shows how to integrate the Google Cast SDK into your application. We provide a CaaS (Cast
as a Service) object, which not only contains setup() and cast() functions, but also contains a cast
event listener, allowing you to complete this feature conveniently.

# Prerequisites

To implement CaaS (Cast as a Service) in your app, please follow the steps outlined in the [*
*BasicPlayback
**](https://github.com/BlendVision/Android-Player-SDK/tree/feature/integrate_sample/BasicPlayback)
section.

# Basic Usage
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

   <img width="261" height="524" alt="portfolio_view" src="https://github.com/BlendVision/Android-Player-SDK/assets/129143433/fb86d181-e3ca-4bdc-acb0-5ef9f805f136">

   If you don't like default activity `CaaSExpandedController`, just set a activity whatever you
   wanted but it must extend `ExpandedControllerActivity`

2. Add meta-data in your AndroidManifest
   ```xml
    <!--This meta data is used for Google Cast framework-->
     <meta-data
        android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
        android:value="package.path.to.your.CastOptionsProvider"/>
   ```

3. Create MediaRouteButton layout.
   ```xml
    <!--R.layout.button_cast-->
    <?xml version="1.0" encoding="utf-8"?>
    <androidx.mediarouter.app.MediaRouteButton
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/castButton"
        android:layout_width="@dimen/btn_size"
        android:layout_height="@dimen/btn_size"
        android:layout_gravity="center_vertical"
        app:mediaRouteButtonTint="@color/white" />
   ```
4. Inflate MediaRouteButton layout.
   ```kotlin
    val castButton = LayoutInflater.from(context).inflate(R.layout.button_cast, parnet, false) as MediaRouteButton
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
<fragment android:id="@+id/castMiniController" android:layout_width="0dp"
        android:layout_height="wrap_content" android:layout_alignParentBottom="true"
        android:visibility="gone" class="com.kkstream.playcraft.caas.player.CaaSMiniController"
        app:layout_constraintEnd_toEndOf="parent" app:layout_constraintBottom_toBottomOf="parent" />
```
