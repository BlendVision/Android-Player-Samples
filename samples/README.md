## Project Requirements
- In the manifest file of your application also provide your UniPlayer license key by adding a `meta-data` tag inside the `application` tag. 
```xml=
<meta-data
    android:name="UNI_PLAYER_LICENSE_KEY"
    android:value="${YOUR_OWN_LICENSE_KEY}" />
```
- You can also set the license key with `PlayerConfig`. The default priority is PlayerConfig first, AndroidManifest second.
```kotlin=
player = UniPlayer.Builder(
    context = this,
    playerConfig = PlayerConfig(
        license = "${YOUR_OWN_LICENSE_KEY}",
        playLogger = object : PlayLogger {
            override fun logEvent(eventName: String, properties: Map<String, Any>) {
                // do something
            }
        }
    )
).build()
```
*Note: if you didn't set the correct license key, you will get the 20403 error.*

## Available Sample Apps
Every example is available in `Kotlin` :+1:

### Basics
+   **BasicApi:** Shows how to get the media content with RESTful API from the server.
+   **BasicPlayback:** Shows how the UniPlayer can be setup for basic playback of DASH with DRM.