## Integration Guide

This guide shows how to send player logs to the player's default analysis service by setting the
AnalyticsConfig when creating the player.

# Prerequisites

To implement analytics capabilities in your app, please follow the steps outlined in the [**BasicPlayback**](https://github.com/BlendVision/Android-Player-SDK/tree/main/BasicPlayback) section.  

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




