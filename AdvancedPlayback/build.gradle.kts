plugins {
    id("com.blendvision.player.sample.plugin.android.application.config")
}

android {
    namespace = "com.blendvision.player.advanced.playback.sample"
}

dependencies {
    implementation(library.bundles.player)
    implementation(library.bundles.common)
}