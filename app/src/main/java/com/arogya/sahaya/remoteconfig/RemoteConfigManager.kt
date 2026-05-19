package com.arogya.sahaya.remoteconfig

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigManager {
    private const val TAG = "RemoteConfigManager"

    fun init() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // Fetch once per hour
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        // Default values can be set here if needed
        // remoteConfig.setDefaultsAsync(mapOf("key" to "value"))

        fetchAndActivate()
    }

    fun fetchAndActivate() {
        Firebase.remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                } else {
                    Log.d(TAG, "Config fetch failed")
                }
            }
    }

    fun getString(key: String): String {
        return Firebase.remoteConfig.getString(key)
    }
}
