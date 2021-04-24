package com.crazie.android.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazie.android.R
import com.crazie.android.bottomsheet.ShareBottomSheet
import com.crazie.android.fragment.AccountFragment
import com.crazie.android.fragment.LikedUsersFragment
import com.crazie.android.model.Post
import com.crazie.android.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedRecyclerViewAdapter(private val postList: ArrayList<Post>)
    : RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var postShare:ImageView
        private lateinit var postLike:ImageView
        private lateinit var doubleTapmiddleLike:ImageView
        private lateinit var postLikesCount:TextView

        private lateinit var menuPost:ImageView

        private var doubleTap:Boolean = false
        private lateinit var animDoubleTap:Animation


        val postUserName = itemView.findViewById<TextView>(R.id.post_username)
        val postPublisher = itemView.findViewById<TextView>(R.id.post_publisher)
        val postImage = itemView.findViewById<ImageView>(R.id.post_image)
        val postCaption = itemView.findViewById<TextView>(R.id.post_caption)

        val publisherDP = itemView.findViewById<ImageView>(R.id.publisher_dp)

        fun bindItems(post: Post) {
            postLikesCount = itemView.findViewById(R.id.post_likes_count)
            doubleTapmiddleLike = itemView.findViewById(R.id.double_tap_like_icon_middle)

            postShare = itemView.findViewById(R.id.post_share)
            postLike = itemView.findViewById(R.id.post_like)

            menuPost = itemView.findViewById(R.id.options_menu_post)

            postShare.setOnClickListener {
                ShareBottomSheet().show((itemView.context as FragmentActivity)
                        .supportFragmentManager,"SHareBottomSheet")
            }

            publisherInfo(post,post.publisher)
            isLikes(post.postId)
            mLikes(postLikesCount,post.postId)

            postUserName.setOnClickListener {
                (itemView.context as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, AccountFragment(post.publisher)).commit()
            }

            postLike.setOnClickListener {
                if (postLike.tag == "Like"){
                    giveLike(post.postId,true)
                }else{
                    giveLike(post.postId,false)
                }

            }

            postLikesCount.setOnClickListener {
                (itemView.context as FragmentActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, LikedUsersFragment(post.postId)).commit()
            }

            val handler = Handler(Looper.myLooper()!!)
            val runnable = Runnable {
                doubleTap = false
            }
            postImage.setOnClickListener {
                animDoubleTap = AnimationUtils
                        .loadAnimation(itemView.context,R.anim.anim_double_tap_like)

                    if (doubleTap){
                    doubleTap = !doubleTap
                    doubleTapmiddleLike.startAnimation(animDoubleTap)
                    doubleTapmiddleLike.visibility = View.VISIBLE
                    Handler(Looper.myLooper()!!).postDelayed({
                        doubleTapmiddleLike.visibility = View.GONE
                    },600)
                    //postLike.startAnimation(animDoubleTap)
                    giveLike(post.postId,true)
                    postLike.setImageResource(R.drawable.ic_liked)
                    handler.removeCallbacks(runnable)
                }else{
                    doubleTap = !doubleTap
                    handler.postDelayed(runnable,500)
                }
            }


        }

        private fun giveLike(postId: String,isLiked:Boolean){
            if (isLiked){
                FirebaseDatabase.getInstance().getReference("likes")
                        .child(postId)
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(isLiked)
            }else{
                FirebaseDatabase.getInstance().getReference("likes")
                        .child(postId)
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .removeValue()
            }
        }

        private fun isLikes(postId:String){
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase.getInstance().getReference("likes").child(postId)
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(firebaseUser!!.uid).exists()){
                        postLike.setImageResource(R.drawable.ic_liked)
                        postLike.tag = "Liked"
                    }else{
                        postLike.setImageResource(R.drawable.ic_like)
                        postLike.tag = "Like"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        private fun mLikes(likes:TextView, postId: String){
            val ref = FirebaseDatabase.getInstance().getReference("likes").child(postId)
            ref.addValueEventListener(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount > 0){
                        likes.text = snapshot.childrenCount.toString() + " Likes"
                        likes.visibility = View.VISIBLE
                    }else{
                        likes.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }


        private fun publisherInfo(post: Post,userId:String) {
            val dataBase = FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(userId)


            Glide.with(itemView).load(post.postImage).into(postImage)
            if (post.postCaption.isNotEmpty()){
                postCaption.visibility = View.VISIBLE
                postCaption.text = post.postCaption
                var moreEnable = true

                postCaption.setOnClickListener {
                    if (moreEnable){
                        postCaption.ellipsize = null
                    }else{
                        postCaption.maxLines = 2
                        postCaption.ellipsize = TextUtils.TruncateAt.END
                    }
                    moreEnable = !moreEnable
                }

            }else{
                postCaption.visibility = View.GONE
                postPublisher.visibility = View.GONE
            }
            val valueEventListener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        postUserName.text = user.userName
                        postPublisher.text = user.userName

                        if (user.photoUrl != "null"){
                            Glide.with(itemView).load(user.photoUrl).into(publisherDP)
                        }

                        if (user.isAccountVerified){
                            postUserName.setCompoundDrawablesWithIntrinsicBounds(
                                    0,0,R.drawable.ic_verified,0)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(itemView.context
                            , "Something went wrong $error", Toast.LENGTH_SHORT).show()
                }

            }
            dataBase.addValueEventListener(valueEventListener)


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_post,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(postList[position])
    }

    override fun getItemCount(): Int = postList.size


}