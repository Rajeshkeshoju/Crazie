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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowingListFragment(private val uId: String) : Fragment() {

    private lateinit var followingList:RecyclerView
    private  var followingUsers = ArrayList<SearchUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=  inflater.inflate(R.layout.fragment_following_list, container, false)
        bindViews(v)
        init()

        return v
    }

    private fun bindViews(v:View) {
        followingList = v.findViewById(R.id.recyclerview_following_list)
    }

    private fun init() {
        followingList.setHasFixedSize(true)
        followingList.layoutManager = LinearLayoutManager(context)
        val adapter = SearchUserAdapter(context,followingUsers)
        followingList.adapter = adapter

        FirebaseDatabase.getInstance().getReference("follow")
            .child(uId)
            .child("following")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    followingUsers.clear()
                    for (ss in snapshot.children){
                        val uId = ss.key
                        FirebaseDatabase.getInstance().getReference("users")
                            .child(uId.toString())
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(SearchUser::class.java)
                                    user?.let { followingUsers.add(it) }

                                    adapter.notifyDataSetChanged()
                                }


                                override fun onCancelled(error: DatabaseError) {
                                }

                            })
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context
                        ,"Something went wrong", Toast.LENGTH_SHORT).show()
                }

            })
    }
}