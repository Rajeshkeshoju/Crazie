package com.crazie.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.crazie.android.R
import com.crazie.android.utils.UtilCheckConnectivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!UtilCheckConnectivity().isOnline()){
            setContentView(R.layout.activity_splash)
            startActivity(Intent(this, NoInternetActivity::class.java))
            finish()
        }else {
            setContentView(R.layout.activity_splash)
            init()
        }

    }

    private fun init() {
            FirebaseRemoteConfig.getInstance().apply {
                val configSettings = FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(0)
                        .build()
                setConfigSettingsAsync(configSettings)
                setDefaultsAsync(R.xml.remote_config_defaults)

                fetchAndActivate().addOnCompleteListener {
                    splashEnableDisable(getBoolean("splash_enabled"))
                }

            }

    }

    private fun splashEnableDisable(result: Boolean) {
        if (result){
            Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 2000)

        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }



}