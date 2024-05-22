plugins {
    id("com.blendvision.player.sample.plugin.android.application.config")
}

android {
    namespace = "com.blendvision.player.basic.playback.tvsample"
}

dependencies {
    implementation(library.bundles.player)
    implementation(library.bundles.common)
}