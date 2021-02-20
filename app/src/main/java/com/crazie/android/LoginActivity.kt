package com.crazie.android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crazie.android.bottomsheet.RegisterBottomSheet
import com.crazie.android.transitionbutton.TransitionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var createAccount:TextView

    private lateinit var loginEmail:TextInputLayout
    private lateinit var loginPassword:TextInputLayout
    private lateinit var btnLogin:TransitionButton

    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindViews()
        clickListeners()
        init()

    }

    private fun init() {
        auth = FirebaseAuth.getInstance()

    }



    private fun bindViews() {
        createAccount = findViewById(R.id.tv_create_account)
        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        btnLogin = findViewById(R.id.btn_login)
    }

    private fun clickListeners() {
        createAccount.setOnClickListener (this)
        btnLogin.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.tv_create_account ->
                    RegisterBottomSheet().show(supportFragmentManager,"Create Account")

                R.id.btn_login ->{
                    btnLogin.startAnimation()
                    mEmail = loginEmail.editText?.text.toString().trim()
                    mPassword = loginPassword.editText?.text.toString().trim()

                    if (mEmail.isNotEmpty() && mPassword.isNotEmpty()){
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                            loginEmail.editText!!.error = "Invalid Email"

                            btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)
                        }else {
                            loginUser(mEmail, mPassword)
                        }

                    }else{
                        btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)
                    }
                }


            }
        }
    }

    private fun loginUser(mEmail: String, mPassword: String) {
        auth.signInWithEmailAndPassword(mEmail,mPassword)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)

                }
            }
    }

    override fun onStart() {

        val user = auth.currentUser
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        super.onStart()
    }
}