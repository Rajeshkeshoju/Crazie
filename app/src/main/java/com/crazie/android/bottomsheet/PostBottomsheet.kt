package com.crazie.android.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazie.android.R
import com.crazie.android.fragment.PostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PostBottomsheet : BottomSheetDialogFragment() , View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.bottomsheet_post,container,false)

        val addPost = view.findViewById<TextView>(R.id.add_post)
        val addStory = view.findViewById<TextView>(R.id.add_story)

        addPost.setOnClickListener(this)
        addStory.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.add_post -> {
                    dismiss()
                    activity!!.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container,PostFragment())
                            .commit()
                }
                R.id.add_story -> { dismiss()}
            }
        }
    }
}