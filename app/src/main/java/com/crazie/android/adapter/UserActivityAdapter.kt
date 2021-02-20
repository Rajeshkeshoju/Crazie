package com.crazie.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazie.android.R
import com.crazie.android.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivityAdapter(private val context: Context,
                private var userActivityList:ArrayList<Post>): RecyclerView.Adapter<UserActivityAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private  var otherUserImage : ImageView = itemView.findViewById(R.id.other_user_image)
        private  var otherUserPost: ImageView = itemView.findViewById(R.id.other_user_post)
        private  var userActivityTextView: TextView = itemView.findViewById(R.id.user_activity_text)

        fun bindItems(userActivity: Post){
            FirebaseDatabase.getInstance().getReference("users").child(userActivity.publisher)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Glide.with(itemView).load(snapshot.child("photoUrl").value).into(otherUserImage)
                            Glide.with(itemView).load(userActivity.postImage).into(otherUserPost)
                            userActivityTextView.text = itemView.context.getString(R.string.user_activity_text,snapshot.child("userName").value)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_user_activity,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userActivityList[position])
    }

    override fun getItemCount(): Int = userActivityList.size
}