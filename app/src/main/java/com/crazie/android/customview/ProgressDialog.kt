package com.crazie.android.customview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.crazie.android.R

class ProgressDialog{
    companion object{

        @SuppressLint("StaticFieldLeak")
        private lateinit var progressBar: ProgressBar
        private lateinit var dialog : Dialog

        @SuppressLint("InflateParams", "ResourceType")
        fun progressDialog(context: Context, activity: Activity) : Companion {

//            progressBar = activity.findViewById(R.id.progress_bar)

            dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog,null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return this
        }

        fun setProgress(progress:Double) {
            progressBar.progress = progress.toInt()
        }

        fun setTitle(s: String) {
            dialog.setTitle(s)
        }

        fun show() {
            dialog.show()
        }

        fun dismiss() {
            progressBar.visibility = View.GONE
            dialog.dismiss()
        }

    }
}