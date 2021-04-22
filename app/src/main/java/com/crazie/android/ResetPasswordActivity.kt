package com.crazie.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        findViewById<Button>(R.id.btn_send_reset_password_link).setOnClickListener {
            val email = findViewById<TextInputLayout>(R.id.et_reset_email)
                    .editText?.text.toString().trim()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this,"Reset link sent to your email",Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}