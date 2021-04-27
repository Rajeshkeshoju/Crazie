package com.crazie.android.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.crazie.android.R
import com.crazie.android.utils.toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        findViewById<TextInputLayout>(R.id.et_reset_email).editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()){
                    findViewById<Button>(R.id.btn_send_reset_password_link).visibility = View.VISIBLE
                }else{
                    findViewById<Button>(R.id.btn_send_reset_password_link).visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        findViewById<Button>(R.id.btn_send_reset_password_link).setOnClickListener {
            val email = findViewById<TextInputLayout>(R.id.et_reset_email)
                    .editText?.text.toString().trim()

            if (email.isNotEmpty()){
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    findViewById<TextInputLayout>(R.id.et_reset_email).editText?.error = "Invalid Email"
                }else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    toast("Reset link sent to your email")
                                    finish()
                                }else{
                                    toast("Something went wrong")
                                }
                            }
                }

            }

        }
    }
}