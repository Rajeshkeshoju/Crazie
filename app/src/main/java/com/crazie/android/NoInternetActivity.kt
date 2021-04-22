package com.crazie.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.crazie.android.utils.UtilCheckConnectivity

class NoInternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_no_internet)

        findViewById<Button>(R.id.btn_try_again_connection).setOnClickListener {
            checkAgain()
        }

    }

    private fun checkAgain() {
        if (UtilCheckConnectivity().isOnline()){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }


}