## Integration Guide

This guide offers comprehensive instructions for integrating analytics into your Android
application.

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

You can initialize `AnalyticsConfig` with a variety of settings including resource ID, user ID,
session ID, tags, and custom data.

```kotlin
val analyticsConfig = AnalyticsConfig.Builder("YOUR_TOKEN") // token is required
   .setResourceId("I am resource id.")  // optional
   .setUserId("I am user id.")          // optional
   .setSessionId("I am session id.")    // optional
   .setTags(listOf("analytics", "1.0.0")) // optional
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




