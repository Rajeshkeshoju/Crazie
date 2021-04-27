package com.crazie.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazie.android.R
import com.crazie.android.adapter.SearchUserAdapter
import com.crazie.android.model.SearchUser
import com.crazie.android.utils.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowersListFragment(private val uId: String) : Fragment() {

    private lateinit var followersList: RecyclerView
    private  var followersUsers = ArrayList<SearchUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=  inflater.inflate(R.layout.fragment_followers_list, container, false)
        bindViews(v)
        init()

        return v
    }

    private fun bindViews(v:View) {
        followersList = v.findViewById(R.id.recyclerview_followers_list)
    }

    private fun init() {
        followersList.setHasFixedSize(true)
        followersList.layoutManager = LinearLayoutManager(context)
        val adapter = SearchUserAdapter(context,followersUsers)
        followersList.adapter = adapter

        FirebaseDatabase.getInstance().getReference("follow")
                .child(uId)
                .child("followers")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followersUsers.clear()
                        for (ss in snapshot.children){
                            val uId = ss.key
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(uId.toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val user = snapshot.getValue(SearchUser::class.java)
                                            user?.let { followersUsers.add(it) }

                                            adapter.notifyDataSetChanged()
                                        }


                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view!!.context.toast("Something went wrong")
                    }

                })
    }

}