object BuildVersion {
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
}

private object LibsVersions {
    const val constraintlayoutVersion = "2.1.4"
    const val castFrameworkVersion = "21.1.0"
    const val lifecycleVersion = "2.5.1"
    const val coroutinesVersion = "1.7.3"
    const val koinVersion = "3.4.3"
    const val retrofit2Version = "2.9.0"
    const val gsonVersion = "2.10.1"
    const val okhttpLoggingInterceptor = "4.9.0"
    const val glideVersion = "4.15.1"
    const val coreKtxVersion = "1.9.0"
    const val appcompatVersion = "1.6.1"
    const val materialVersion = "1.9.0"
    const val junitVersion = "4.13.2"
    const val extJunitVersion = "1.1.5"
    const val espressoCoreVersion = "3.5.1"
    const val activityVersion = "1.6.1"
    const val guavaVersion = "27.1-android"
    const val imaVersion = "3.24.0"
}

object Libs {

    const val IMA = "com.google.ads.interactivemedia.v3:interactivemedia:${LibsVersions.imaVersion}"

    const val guava = "com.google.guava:guava:${LibsVersions.guavaVersion}"

    const val activity_ktx = "androidx.activity:activity-ktx:${LibsVersions.activityVersion}"

    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${LibsVersions.constraintlayoutVersion}"

    const val cast_framework =
        "com.google.android.gms:play-services-cast-framework:${LibsVersions.castFrameworkVersion}"

    //ViewModel
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibsVersions.lifecycleVersion}"

    //LiveData
    const val lifecycle_livedata_ktx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${LibsVersions.lifecycleVersion}"

    //Lifecycles only (without ViewModel or LiveData)
    const val lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${LibsVersions.lifecycleVersion}"

    //gson
    const val gson = "com.google.code.gson:gson:${LibsVersions.gsonVersion}"

    //Glide
    const val glide = "com.github.bumptech.glide:glide:${LibsVersions.glideVersion}"

    //coroutines
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibsVersions.coroutinesVersion}"

    //koin
    const val koin = "io.insert-koin:koin-android:${LibsVersions.koinVersion}"

    //retrofit2
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${LibsVersions.retrofit2Version}"
    const val converterGson =
        "com.squareup.retrofit2:converter-gson:${LibsVersions.retrofit2Version}"

    //okhttp3 logging-interceptor
    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${LibsVersions.okhttpLoggingInterceptor}"

    const val core_ktx = "androidx.core:core-ktx:${LibsVersions.coreKtxVersion}"
    const val appcompat = "androidx.appcompat:appcompat:${LibsVersions.appcompatVersion}"
    const val material = "com.google.android.material:material:${LibsVersions.materialVersion}"
    const val junit = "junit:junit:${LibsVersions.junitVersion}"
    const val ext_junit = "androidx.test.ext:junit:${LibsVersions.extJunitVersion}"
    const val espresso_core =
        "androidx.test.espresso:espresso-core:${LibsVersions.espressoCoreVersion}"

}