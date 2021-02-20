package com.crazie.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazie.android.R
import com.crazie.android.model.Post

class AccountPostAdapter(private val posts: ArrayList<Post>)
    : RecyclerView.Adapter<AccountPostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var postThumbnail: ImageView

        fun bindItems(post: Post) {
            postThumbnail = itemView.findViewById(R.id.post_thumbnail)

            Glide.with(itemView).load(post.postImage).into(postThumbnail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_post_thumbnail,parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (posts.isNotEmpty()){
            holder.bindItems(posts[position])
        }
    }

    override fun getItemCount(): Int = posts.size
}
