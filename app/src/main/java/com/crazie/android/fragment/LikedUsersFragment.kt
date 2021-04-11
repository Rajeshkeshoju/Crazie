package com.crazie.android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazie.android.R
import com.crazie.android.adapter.SearchUserAdapter
import com.crazie.android.model.SearchUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LikedUsersFragment(private val postId:String) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:SearchUserAdapter
    private  var likedUsers = ArrayList<SearchUser>()

    private lateinit var toolbar: Toolbar

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_liked_users, container, false)

        toolbar = v.findViewById(R.id.toolbar_post_liked_users)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "Likes"
        toolbar.findViewById<ImageView>(R.id.arrow_back).setOnClickListener {
            fragmentManager?.popBackStack()
        }

        recyclerView = v.findViewById(R.id.rv_liked_users)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL,false)

        adapter = SearchUserAdapter(context,likedUsers)
        recyclerView.adapter = adapter

        showLikes()
        return v
    }

    private fun showLikes(){
        FirebaseDatabase.getInstance().getReference("likes").child(postId)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        likedUsers.clear()
                        for (ss in snapshot.children)
                        {

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(ss.key.toString())
                                    .addValueEventListener(object :ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val user = snapshot.getValue(SearchUser::class.java)
                                            if (user != null) {
                                                likedUsers.add(user)
                                            }

                                            adapter.notifyDataSetChanged()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })

                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
    }
}