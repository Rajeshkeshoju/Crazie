package com.crazie.android.utils

import com.crazie.android.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

fun setupRemoteConfig(firebaseRemoteConfig: FirebaseRemoteConfig) {
    firebaseRemoteConfig.apply {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }
}