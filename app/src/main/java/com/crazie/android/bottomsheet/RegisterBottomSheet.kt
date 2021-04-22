package com.crazie.android.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.crazie.android.MainActivity
import com.crazie.android.R
import com.crazie.android.model.User
import com.crazie.android.utils.UtilCheckConnectivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.royrodriguez.transitionbutton.TransitionButton
import java.util.*

class RegisterBottomSheet : BottomSheetDialogFragment() , View.OnClickListener{
    private lateinit var fullName : TextInputLayout
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var confirmPassword : TextInputLayout

    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private lateinit var mName: String
    private lateinit var mUserName: String
    private lateinit var mConfirmPassword: String

    private lateinit var btnRegister : TransitionButton
    private lateinit var close : ImageView

    private lateinit var auth:FirebaseAuth
    private lateinit var dataBase:DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.setOnShowListener{
            val bottomSheetDialog = it as BottomSheetDialog
            val sheetInternal: View =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state=  BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(sheetInternal).peekHeight = sheetInternal.height
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         val v = inflater.inflate(R.layout.layout_register,container,false)

        init()
        bindViews(v)

        clickListeners()

        return v
    }

    private fun clickListeners() {
        btnRegister.setOnClickListener(this)
        close.setOnClickListener(this)
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference
    }

    private fun bindViews(v:View) {
        fullName = v.findViewById(R.id.register_full_name)
        email = v.findViewById(R.id.register_email)
        password = v.findViewById(R.id.register_password)
        confirmPassword = v.findViewById(R.id.register_confirm_password)

        btnRegister = v.findViewById(R.id.btn_create_account)
        close = v.findViewById(R.id.register_close)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_create_account -> {

                if(UtilCheckConnectivity().isOnline()){

                    btnRegister.startAnimation()
                    mName = fullName.editText?.text.toString()
                    mUserName = fullName.editText?.text
                            .toString()
                            .filter { !it.isWhitespace() }
                            .toLowerCase(Locale.ROOT)
                    mEmail = email.editText?.text.toString().trim()
                    mPassword = password.editText?.text.toString().trim()
                    mConfirmPassword = confirmPassword.editText?.text.toString().trim()
                    if(mPassword == mConfirmPassword){
                        if (mEmail.isNotEmpty()){
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                                email.editText!!.error = "Invalid Email"

                                btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)
                            }else{
                                createAccount(mEmail,mPassword)
                            }
                        }else{
                            btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)
                        }

                    }

                }else{
                    Toast.makeText(context,R.string.no_internet,Toast.LENGTH_SHORT).show()
                }


            }

            R.id.register_close ->
                dismiss()
        }
    }



    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val u = auth.currentUser
                    val user = User(u?.uid,mName,mUserName,mEmail)

                    dataBase.child("users").child(u?.uid.toString()).setValue(user)

                    btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                        startActivity(
                            Intent(activity, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        )
                        activity?.finish()
                    }

                }else{

                    btnRegister.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE,null)

                    if(it.exception is FirebaseAuthUserCollisionException){
                        Toast.makeText(context,"User already registered", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

}