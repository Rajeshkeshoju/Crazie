package com.crazie.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazie.android.R
import com.crazie.android.fragment.AccountFragment
import com.crazie.android.model.SearchUser
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SearchUserAdapter(context: Context?, users: ArrayList<SearchUser>)
    : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    private  var mContext: Context? = context
    private var mUsers:ArrayList<SearchUser> = users


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var userImageView: ImageView
        private lateinit var userName: TextView
        private lateinit var userFullName: TextView
        private lateinit var userFollowUnFollow:MaterialButton

        private lateinit var firebaseUser: FirebaseUser
        private lateinit var dataBase: DatabaseReference

        fun bindItems(searchUser: SearchUser){

            userImageView = itemView.findViewById(R.id.search_result_user_image)
            userName = itemView.findViewById(R.id.search_result_username)
            userFullName = itemView.findViewById(R.id.search_result_user_fullname)
            userFollowUnFollow = itemView.findViewById(R.id.btn_search_user_follow)


            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            userName.text = searchUser.userName
            userFullName.text = searchUser.fullName

            if (searchUser.isAccountVerified){
                userName.setCompoundDrawablesWithIntrinsicBounds(
                        0,0,R.drawable.ic_verified,0)
            }

            if(searchUser.photoUrl != "null"){
                Glide.with(itemView).load(searchUser.photoUrl).into(userImageView)
            }

            if (searchUser.uId.equals(firebaseUser.uid)){
                userFollowUnFollow.visibility  = View.GONE
            }else{
                userFollowUnFollow.visibility= View.VISIBLE
            }

            isFollowing(searchUser.uId,userFollowUnFollow)

            itemView.setOnClickListener {
                val editor = itemView.context.getSharedPreferences("PREFS",
                        Context.MODE_PRIVATE).edit()
                editor.putString("userId",searchUser.uId)
                editor.apply()

                (itemView.context as FragmentActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                AccountFragment(searchUser.uId.toString())).commit()
            }

            userFollowUnFollow.setOnClickListener {
                if (userFollowUnFollow.text == "Follow"){
                    FirebaseDatabase.getInstance().reference
                    .child("follow")
                            .child(searchUser.uId.toString())
                            .child("followers")
                            .child(firebaseUser.uid)
                            .setValue(true)
                    FirebaseDatabase.getInstance().reference
                    .child("follow")
                            .child(firebaseUser.uid)
                            .child("following")
                            .child(searchUser.uId.toString())
                            .setValue(true)
                }else{
                    FirebaseDatabase.getInstance().reference
                    .child("follow")
                            .child(searchUser.uId.toString())
                            .child("followers")
                            .child(firebaseUser.uid)
                            .removeValue()
                    FirebaseDatabase.getInstance().reference
                    .child("follow")
                            .child(firebaseUser.uid)
                            .child("following")
                            .child(searchUser.uId.toString())
                            .removeValue()

                    userFollowUnFollow.backgroundTintList = ContextCompat.getColorStateList(
                            itemView.context,
                            android.R.color.white
                    )
                }
            }
        }

        private fun isFollowing(userId:String?, buttonFollow:MaterialButton){
            dataBase = FirebaseDatabase.getInstance().reference
                    .child("follow")
                    .child(firebaseUser.uid)
                    .child("following")

            dataBase.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (userId?.let { snapshot.child(it).exists() } == true){
                        buttonFollow.text = itemView.context.getString(R.string.following)
                    }else{
                        buttonFollow.text = itemView.context.getString(R.string.follow)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(itemView.context
                            ,"Something went wrong", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(
                R.layout.layout_search_result,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mUsers[position])
    }

    override fun getItemCount(): Int = mUsers.size


}