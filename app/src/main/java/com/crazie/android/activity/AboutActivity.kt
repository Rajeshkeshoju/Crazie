package com.crazie.android.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.crazie.android.R

class AboutActivity : AppCompatActivity() {

    private lateinit var textContribute: TextView

    private val contributionURL = "https://github.com/Rajeshkeshoju/Crazie"
    private val contributionText = "Contribute code to <u><font color='aqua'>GitHub/Crazie</font></u>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        textContribute = findViewById(R.id.tv_contribute)
        textContribute.text = HtmlCompat.fromHtml(contributionText,HtmlCompat.FROM_HTML_MODE_LEGACY)

        textContribute.setOnClickListener {
            val contributionIntent = Intent(Intent.ACTION_VIEW)
            contributionIntent.data = Uri.parse(contributionURL)
            startActivity(contributionIntent)
        }
    }

}