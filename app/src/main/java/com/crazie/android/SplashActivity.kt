package com.crazie.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawableResource(R.mipmap.ic_launcher_round)

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_splash)

        startActivity(Intent(this,LoginActivity::class.java))
        finish()
        //init()
        /*if (!UtilCheckConnectivity().isOnline()){
            setContentView(R.layout.activity_splash)
            startActivity(Intent(this,NoInternetActivity::class.java))
            finish()
        }else {
            setContentView(R.layout.activity_splash)
            init()
        }*/

    }

    /*private fun init() {
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
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }, 2000)

        }else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }*/



}