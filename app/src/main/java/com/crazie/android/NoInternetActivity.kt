package com.crazie.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.crazie.android.utils.UtilCheckConnectivity
import com.google.android.material.button.MaterialButton

class NoInternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_no_internet)

        findViewById<MaterialButton>(R.id.btn_try_again_connection).setOnClickListener {
            checkAgain()
        }

    }

    private fun checkAgain() {
        if (!UtilCheckConnectivity().isOnline()){
            Handler(mainLooper).postDelayed({
                checkAgain()
            },500)
        }else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }


}