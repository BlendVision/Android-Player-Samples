package com.kkstream.basicanalysis.sample.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kkstream.playcraft.analytics.presentation.kksplayer_collector.AnalyticsConfig
import com.kkstream.playcraft.analytics.presentation.kksplayer_collector.KKSPlayerCollector

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var analyticsConfig: AnalyticsConfig
    private var kksPlayerCollector: KKSPlayerCollector

    init {
        analyticsConfig = createAnalyticsConfig()
        kksPlayerCollector = createKKSPlayerCollector(analyticsConfig)
    }

    private fun createAnalyticsConfig(): AnalyticsConfig {
        return AnalyticsConfig.Builder("REPLACE_YOUR_TOKEN")
            .setResourceId("I am resource id.")
            .setUserId("I am user id.")
            .setSessionId("I am session id.")
            .setTags(listOf("analytics", "1.0.0"))
            .setCustomData(mapOf("custom_parameter" to "I am String", "custom_parameter" to 999))
            .build()
    }

    private fun createKKSPlayerCollector(analyticsConfig: AnalyticsConfig): KKSPlayerCollector {
        return KKSPlayerCollector.Builder(getApplication(), analyticsConfig).build()
    }

    fun sendEvent(eventName: String, properties: Map<String, Any>) {
        kksPlayerCollector.sentEvent(eventName, properties)
    }

    override fun onCleared() {
        kksPlayerCollector.release()
        super.onCleared()
    }
}