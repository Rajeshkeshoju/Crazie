package com.crazie.android.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crazie.android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage

class PostFragment : Fragment() {

    private lateinit var imageUri:Uri
    private lateinit var storageRef : StorageReference

    private lateinit var close:ImageView
    private lateinit var postImage:ImageView
    private lateinit var postIt:TextView
    private lateinit var postCaption : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        // Inflate the layout for this fragment

        val v =  inflater.inflate(R.layout.fragment_post, container, false)

        activity?.let {
           val intent = CropImage.activity()
                    .setAspectRatio(1,1)
                    .getIntent(v.context)
            startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        }

        bindViews(v)
        clickListener()

        return v
    }

    private fun bindViews(view:View) {
        close = view.findViewById(R.id.close_post_fragment)
        postIt = view.findViewById(R.id.do_post)
        postImage = view.findViewById(R.id.image_added)
        postCaption = view.findViewById(R.id.et_post_caption)

        storageRef = FirebaseStorage.getInstance().getReference("posts")
    }

    private fun clickListener() {
        close.setOnClickListener {
            loadHome()
        }

        postIt.setOnClickListener { uploadPost()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            postImage.setImageURI(imageUri)
        }else{

            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
            loadHome()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHome() {
        activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,HomeFragment())
                .commit()
    }

    private fun uploadPost() {
//        val dialog = ProgressDialog.progressDialog(this.context!! , activity!!)
//        dialog.setTitle("Uploading")

        val fileRef = storageRef.child(System.currentTimeMillis().toString()+"."+"jpg")
        fileRef.putFile(imageUri)
                .addOnProgressListener {
//                    dialog.show()
//                    var progress = (100.0 * it.bytesTransferred) / it.totalByteCount
//                    dialog.setProgress(progress)
                }
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadUrl = it.result.toString()

                        val dbRef = FirebaseDatabase.getInstance().getReference("posts")
                        val postId = dbRef.push().key
                        val hashMap = HashMap<String, Any>()
                        if (postId != null) {
                            hashMap["postId"] = postId
                        }
                        hashMap["postImage"] = downloadUrl
                        hashMap["postCaption"] = postCaption.text.toString()
                        FirebaseAuth.getInstance().currentUser?.let { it1 ->
                            hashMap.put("publisher",
                                    it1.uid)
                        }

                        if (postId != null) {
                            dbRef.child(postId).setValue(hashMap)
                        }

                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                    //dialog.dismiss()
                    loadHome()
                }



    }

}