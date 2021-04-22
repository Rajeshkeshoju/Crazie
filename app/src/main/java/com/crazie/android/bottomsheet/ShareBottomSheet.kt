package com.crazie.android.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazie.android.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShareBottomSheet : BottomSheetDialogFragment() , View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.bottomsheet_post_share,container,false)

        val share = view.findViewById<TextView>(R.id.add_post_to_story)

        share.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.add_post_to_story -> {
                    dismiss()
                }
            }
        }
    }
}