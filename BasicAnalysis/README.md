## Integration Guide

This guide shows how to send player logs to the player's default analysis service by setting the
AnalyticsConfig when creating the player.

# Prerequisites

To implement analytics capabilities in your app, please follow the steps outlined in the [*
*BasicPlayback
**](https://github.com/BlendVision/Android-Player-SDK/tree/feature/integrate_sample/BasicPlayback)
section.

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

```

> **Note**: After making changes, don't forget to sync your Gradle files to ensure that the project
> compiles successfully.

## Basic Usage

#### Step 1: Create `AnalyticsConfig`

- `YOUR_TOKEN`: The field is required, this appears to be a token or authentication key for analytics purposes.
- `resourceId`: The field is optional, this could be an identifier for a specific resource or source within your application, used for tracking or categorization.
- `sessionId`: The field is optional, this represents a session identifier, likely used to track user sessions or interactions with the application.
- `customData`: The field is optional, this seems to be a placeholder for custom data that you can include in your analytics events, allowing you to send additional information as needed.

```kotlin
val analyticsConfig = AnalyticsConfig.Builder("YOUR_TOKEN") // token is required
   .setResourceId("I am resource id.")  // optional
   .setUserId("I am user id.")          // optional
   .setSessionId("I am session id.")    // optional
   .setCustomData(
      mapOf(
         "custom_parameter" to "I am String",
         "custom_parameter2" to 999
      )
   ) // optional
   .build()
```

#### Step 2: Set AnalyticsConfig to UniPlayer

call setAnalyticsConfig() method from player and inject AnalyticsConfig.

```kotlin
val uniPlayer = UniPlayer.Builder(requireContext(), createPlayConfigFromArgument())
   .setAnalyticsConfig(analyticsConfig)//set analyticsConfig
   .build()
```




