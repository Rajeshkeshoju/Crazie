package com.crazie.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.crazie.android.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var imageUri: Uri

    private lateinit var newName : TextInputLayout
    private lateinit var newBio : TextInputLayout
    private lateinit var save : ImageView
    private lateinit var close : ImageView
    private lateinit var userImage : ImageView

    private lateinit var dataBase:DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var valueEventListener:ValueEventListener

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        bindViews()
        init()

        save.setOnClickListener(this)
        close.setOnClickListener(this)
        userImage.setOnClickListener(this)
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseDatabase.getInstance()
                .reference.child("users").child(auth.currentUser!!.uid)

        valueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                Glide.with(this@EditProfileActivity)
                        .load(user.photoUrl)
                        .into(userImage)

                newName.editText?.setText(user.fullName)
                newBio.editText?.setText(user.bio)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity
                        ,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        }

        dataBase.addValueEventListener(valueEventListener)

    }

    private fun bindViews() {
        newName = findViewById(R.id.edit_profile_new_name)
        newBio = findViewById(R.id.edit_profile_new_bio)
        userImage = findViewById(R.id.edit_profile_user_image)

        close = findViewById(R.id.edit_profile_close)
        save = findViewById(R.id.edit_profile_save)
    }

    override fun onClick(view: View) {

        when (view.id){

            R.id.edit_profile_close -> {
                finish()
            }

            R.id.edit_profile_save -> {
                if (!TextUtils.isEmpty(newName.editText?.text) && !TextUtils.isEmpty(newBio.editText?.text)){
                    dataBase.child("fullName").setValue(newName.editText?.text.toString())
                    dataBase.child("bio").setValue(newBio.editText?.text.toString())
                    finish()
                }else{
                    newName.editText?.error = "Enter Name"
                    newBio.editText?.error = "Enter Bio"
                }
                /*if (imageUri != null){
                    uploadNewImage()
                }*/

            }

            R.id.edit_profile_user_image -> {
                changeUserImage()
            }

        }
    }

    /*private fun uploadNewImage() {
        val storageRef = FirebaseStorage.getInstance().getReference("userProfilePictures")
        val fileRef = storageRef.child(System.currentTimeMillis().toString()+"."+"jpg")

        val uploadTask = fileRef.putFile(imageUri)
        uploadTask.continueWithTask {
            if (!it.isSuccessful){
                throw it.exception!!
            }

            return@continueWithTask fileRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val downloadUrl = it.result.toString()

                FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("photoUrl")
                        .setValue(downloadUrl)
            }
        }

    }*/

    private fun changeUserImage() {
        val imagePick = Intent(Intent.ACTION_GET_CONTENT)
        imagePick.type = "image/*"
        startActivityForResult(Intent.createChooser(imagePick,"Select image"),
                REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageUri = data?.data!!
        userImage.setImageURI(imageUri)
    }

    companion object {
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    override fun onDestroy() {
        dataBase.removeEventListener(valueEventListener)
        super.onDestroy()
    }
}