package com.crazie.android.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crazie.android.R
import com.crazie.android.adapter.FeedRecyclerViewAdapter
import com.crazie.android.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var feedRecyclerView: RecyclerView
  //  private lateinit var homeStoriesRecyclerView: RecyclerView
    private lateinit var homeToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var directMessage: ImageView

    private lateinit var refreshFeed : SwipeRefreshLayout

    private val posts = ArrayList<Post>()
    private val followingList = ArrayList<String>()

    private lateinit var feedAdapter:FeedRecyclerViewAdapter
    //private lateinit var homeStoriesAdapter:HomeStoriesAdapter


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = LayoutInflater.from(context).inflate(R.layout.fragment_home, container, false)


        bindViews(v)

        directMessage.setOnClickListener {
            Toast.makeText(v.context,"DM",Toast.LENGTH_SHORT).show()
        }

        refreshFeed.setOnRefreshListener {
            fetchPosts()
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                refreshFeed.isRefreshing = false
            },1000)
        }


        val llm = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        feedRecyclerView.layoutManager = llm
        feedRecyclerView.hasFixedSize()

        /*homeStoriesRecyclerView.layoutManager = llm
        homeStoriesRecyclerView.hasFixedSize()*/


        feedAdapter = FeedRecyclerViewAdapter(posts)
        feedRecyclerView.adapter = feedAdapter

        /*homeStoriesRecyclerView = HomeStoriesAdapter()
        homeStoriesRecyclerView.adapter = feedAdapter*/


        checkFollowing()

        return v
    }

    private fun bindViews(v:View) {
        homeToolbar = v.findViewById(R.id.toolbar_home)
        directMessage =  homeToolbar.findViewById(R.id.toolbar_direct_message)

        feedRecyclerView = v.findViewById(R.id.recycler_feed)
        refreshFeed = v.findViewById(R.id.refresh_feed)
    }

    private fun checkFollowing(){
        val ref = FirebaseDatabase.getInstance().getReference("follow")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("following")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                followingList.clear()
                for (ss in snapshot.children){
                    ss.key?.let { followingList.add(it) }
                }

                fetchPosts()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context
                        ,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun fetchPosts(){
        val postRef = FirebaseDatabase.getInstance().getReference("posts")

        postRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                posts.clear()

                for (ss in snapshot.children){
                    val post = ss.getValue(Post::class.java)

                    for (id in followingList){
                        if (post?.publisher?.equals(id) == true){
                            posts.add(post)
                        }
                    }
                }

                feedAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context
                        ,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })
    }


}