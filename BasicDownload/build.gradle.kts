plugins {
    id("com.blendvision.player.sample.plugin.android.application.config")
}

android {
    namespace = "com.blendvision.player.basic.download.sample"
}

dependencies {
    implementation(library.bundles.player)
    implementation(library.bundles.common)
}