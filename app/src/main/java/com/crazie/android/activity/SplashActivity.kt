package com.crazie.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.crazie.android.R
import com.crazie.android.utils.UtilCheckConnectivity
import com.crazie.android.utils.setupRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!UtilCheckConnectivity().isOnline()){
            startActivity(Intent(this, NoInternetActivity::class.java))
            finish()
        }else {
            init()
        }

    }

    private fun init() {
            FirebaseRemoteConfig.getInstance().apply {
                setupRemoteConfig(this)

                fetchAndActivate().addOnCompleteListener {
                    splashEnableDisable(getBoolean("splash_enabled"))
                }

            }

    }

    private fun splashEnableDisable(result: Boolean) {
        if (result){
            Handler(Looper.myLooper()!!).postDelayed({
                setContentView(R.layout.activity_splash)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 2000)

        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }



}